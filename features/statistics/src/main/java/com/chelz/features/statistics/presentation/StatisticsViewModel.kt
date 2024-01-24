package com.chelz.features.statistics.presentation

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.statistics.domain.DatePeriod
import com.chelz.features.statistics.domain.TabState
import com.chelz.features.statistics.presentation.navigation.StatisticsRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.CategoryItem
import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.entity.OperationItem
import com.chelz.shared.accounts.domain.entity.SharedAccount
import com.chelz.shared.accounts.domain.entity.SharedAccountItem
import com.chelz.shared.accounts.domain.entity.SharedCategory
import com.chelz.shared.accounts.domain.entity.SharedOperation
import com.chelz.shared.accounts.domain.entity.toAccountItem
import com.chelz.shared.accounts.domain.entity.toCategoryItem
import com.chelz.shared.accounts.domain.entity.toOperationItem
import com.chelz.shared.accounts.domain.firebase.SharedAccountConstants
import com.chelz.shared.accounts.domain.usecase.account.GetAccountByIdUseCase
import com.chelz.shared.accounts.domain.usecase.account.GetAllAccountsUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetAllCategoriesUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetCategoryByIdUseCase
import com.chelz.shared.accounts.domain.usecase.operation.GetAllOperationsUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.lang.StrictMath.abs
import java.util.LinkedList

class StatisticsViewModel(
	private val getAllAccountsUseCase: GetAllAccountsUseCase,
	private val getAllOperationsUseCase: GetAllOperationsUseCase,
	private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
	private val getAccountByIdUseCase: GetAccountByIdUseCase,
	private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
	private val router: StatisticsRouter,
) : ViewModel() {

	val currentDatePeriodFlow = MutableStateFlow(DatePeriod.DAY7)
	private val auth by lazy { Firebase.auth }
	private val store by lazy { Firebase.firestore }
	private val accountsFlow = MutableStateFlow<List<Account>>(listOf())
	val fullAccountsFlow = MutableStateFlow<List<AccountItem>>(listOf())
	private val operationFlow = MutableStateFlow<List<Operation>>(listOf())
	private val sharedOperationFlow = MutableStateFlow<List<OperationItem>>(listOf())
	val chartOperationFlow = MutableStateFlow<List<OperationItem>>(listOf())
	val categoriesFlow = MutableStateFlow<List<CategoryItem>>(listOf())

	val currentTab = MutableStateFlow<TabState>(TabState.Account)
	val startDateFlow = MutableStateFlow<Long?>(null)
	val endDateFlow = MutableStateFlow<Long?>(null)
	val currentAccount = MutableStateFlow<AccountItem?>(null)
	val currentCategory = MutableStateFlow<CategoryItem?>(null)
	private val sharedAccountsCollection = store.collection(SharedAccountConstants.SHARED_ACCOUNTS_TABLE)

	private val handler = CoroutineExceptionHandler { _, throwable ->
		Log.e("STATS_VM", throwable.message.toString())
	}

	fun init() = viewModelScope.launch {
		updateAccounts().await()
		updateOperation().await()
		updateCategory()

		fullAccountsFlow.value.filterIsInstance<SharedAccountItem>().map { it.operations.toOperationItem() }.flatten()
		sharedAccountsCollection.whereArrayContains(
			SharedAccountConstants.ACCOUNT.USERS, auth.currentUser?.email.toString(),
		).addSnapshotListener { _, error ->
			if (error != null) {
				Log.w(ContentValues.TAG, "Listen failed.", error)
				return@addSnapshotListener
			}
			viewModelScope.launch {
				updateAccounts().await()
				updateOperation().await()
			}
		}
		sharedAccountsCollection.whereEqualTo(
			SharedAccountConstants.ACCOUNT.HOST_EMAIL, auth.currentUser?.email.toString(),
		).addSnapshotListener { _, error ->
			if (error != null) {
				Log.w(ContentValues.TAG, "Listen failed.", error)
				return@addSnapshotListener
			}
			viewModelScope.launch {
				updateAccounts().await()
				updateOperation().await()
			}
		}

		currentAccount.filterNotNull().onEach {
			drawChart()
		}.launchIn(viewModelScope)
		currentCategory.filterNotNull().onEach {
			drawChart()
		}.launchIn(viewModelScope)
	}

	fun drawChart() {
		if (currentTab.value == TabState.Account)
			chartOperationFlow.value = bindAccountData()
		else
			chartOperationFlow.value = bindCategoryData()
	}

	private fun bindAccountData(): List<OperationItem> {
		val result = filterItemsByDatePeriod(currentDatePeriodFlow.value).filter {
			if (currentAccount.value is SharedAccountItem)
				currentAccount.value?.name.toString() in it.account
			else
				it.account == currentAccount.value?.name
		}
		return result
	}

	private fun bindCategoryData(): List<OperationItem> {
		val result = filterItemsByDatePeriod(currentDatePeriodFlow.value).filter {
			it.category?.name == currentCategory.value?.name
		}
		return result
	}

	private suspend fun updateOperation() = viewModelScope.async {
		operationFlow.value = getAllOperationsUseCase()
		val localOperations = toOperationItem(operationFlow.value).toMutableList()
		val sharedAccounts = fullAccountsFlow.value.filterIsInstance<SharedAccountItem>().map { it.operations.toOperationItem() }.flatten()
		localOperations.addAll(sharedAccounts)

		sharedOperationFlow.value = localOperations.sortedByDescending {
			it.date
		}
	}

	private suspend fun updateAccounts() = viewModelScope.async {
		accountsFlow.value = getAllAccountsUseCase()
		val userEmail = auth.currentUser?.email
		val local = accountsFlow.value.toAccountItem()
		val host: List<AccountItem> = getHostAccounts(userEmail).await()
		val shared: List<AccountItem> = getSharedAccounts(userEmail).await()
		val fullList = LinkedList<AccountItem>()
		fullList.addAll(local)
		fullList.addAll(host)
		fullList.addAll(shared)
		fullAccountsFlow.emit(fullList)
	}

	private fun filterItemsByDatePeriod(datePeriod: DatePeriod): List<OperationItem> {
		val items = sharedOperationFlow.value
		val currentDate = DateTime.now()
		val filteredList = when (datePeriod) {
			DatePeriod.DAY7       -> filterItemsByDaysAgo(items, currentDate, 7)
			DatePeriod.DAY14      -> filterItemsByDaysAgo(items, currentDate, 14)
			DatePeriod.DAY30      -> filterItemsByDaysAgo(items, currentDate, 30)
			DatePeriod.DAY_CUSTOM -> filterItemsByCustomRange(items, startDateFlow.value, endDateFlow.value)
		}
		return filteredList
	}

	private fun filterItemsByDaysAgo(items: List<OperationItem>, currentDate: DateTime, daysAgo: Int): List<OperationItem> {
		val pastDate = currentDate.minusDays(daysAgo)
		return items.filter { item ->
			val itemDate = parseDate(item.date)
			itemDate.isAfter(pastDate) || itemDate.isEqual(pastDate)
		}
	}

	private fun filterItemsByCustomRange(items: List<OperationItem>, startDate: Long?, endDate: Long?): List<OperationItem> {
		if (startDate == null || endDate == null) {
			return emptyList()
		}
		val start = DateTime(startDate)
		val end = DateTime(endDate).plusDays(1)
		return items.filter { item ->
			val itemDate = parseDate(item.date)
			itemDate.isAfter(start) && itemDate.isBefore(end) || itemDate.isEqual(start) || itemDate.isEqual(end)
		}
	}

	fun sumValuesByDay(items: List<OperationItem>, checkedEarningValue: Int): Map<LocalDate, Double> {
		val sumMap = mutableMapOf<LocalDate, Double>()

		items.filter { it.quantity * checkedEarningValue < 0 }.forEach { item ->
			val itemDate = parseDate(item.date).toLocalDate()
			val currentValue = sumMap[itemDate] ?: 0.0
			sumMap[itemDate] = currentValue + abs(item.quantity)
		}
		return sumMap
	}

	private fun parseDate(dateString: String): DateTime {
		return DateTime.parse(dateString, org.joda.time.format.DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss.SSS"))
	}

	private suspend fun getHostAccounts(userEmail: String?): Deferred<List<AccountItem>> = viewModelScope.async {
		val query = sharedAccountsCollection.whereEqualTo(
			SharedAccountConstants.ACCOUNT.HOST_EMAIL, userEmail.toString(),
		).get().await()

		query.documents.map { doc ->
			val operations = doc.get(SharedAccountConstants.ACCOUNT.OPERATIONS) as? List<Map<*, *>>

			val fullOperations = operations?.map {
				val categoryMap = it["category"] as Map<*, *>
				val category = SharedCategory(
					name = categoryMap["name"].toString(),
					isEarning = categoryMap["earning"].toString().toBooleanStrict(),
					color = categoryMap["color"].toString(),
				)
				val operation =
					SharedOperation(
						it["name"].toString(),
						it["quantity"].toString().toDouble(),
						category = category,
						it["date"].toString(),
						it["account"].toString(),
					)
				operation
			} ?: emptyList()

			SharedAccount(
				accountId = doc.id,
				name = doc[SharedAccountConstants.ACCOUNT.NAME].toString(),
				number = doc[SharedAccountConstants.ACCOUNT.NUMBER].toString(),
				color = doc[SharedAccountConstants.ACCOUNT.COLOR].toString(),
				money = doc[SharedAccountConstants.ACCOUNT.MONEY].toString().toDouble(),
				hostEmail = doc[SharedAccountConstants.ACCOUNT.HOST_EMAIL].toString(),
				operations = fullOperations
			)
		}.toAccountItem()
	}

	private suspend fun getSharedAccounts(userEmail: String?): Deferred<List<AccountItem>> = viewModelScope.async {
		val query = sharedAccountsCollection.whereArrayContains(
			SharedAccountConstants.ACCOUNT.USERS, userEmail.toString(),
		).get().await()

		query.documents.map { doc ->
			val operations = doc.get(SharedAccountConstants.ACCOUNT.OPERATIONS) as? List<Map<*, *>>

			val fullOperations = operations?.map {
				val categoryMap = it["category"] as Map<*, *>
				val category = SharedCategory(
					name = categoryMap["name"].toString(),
					isEarning = categoryMap["earning"].toString().toBooleanStrict(),
					color = categoryMap["color"].toString(),
				)
				val operation =
					SharedOperation(
						it["name"].toString(),
						it["quantity"].toString().toDouble(),
						category = category,
						it["date"].toString(),
						it["account"].toString(),
					)
				operation
			} ?: emptyList()

			SharedAccount(
				accountId = doc.id,
				name = doc[SharedAccountConstants.ACCOUNT.NAME].toString(),
				number = doc[SharedAccountConstants.ACCOUNT.NUMBER].toString(),
				color = doc[SharedAccountConstants.ACCOUNT.COLOR].toString(),
				money = doc[SharedAccountConstants.ACCOUNT.MONEY].toString().toDouble(),
				hostEmail = doc[SharedAccountConstants.ACCOUNT.HOST_EMAIL].toString(),
				operations = fullOperations
			)
		}.toAccountItem()

	}

	private suspend fun updateCategory() {
		categoriesFlow.value = getAllCategoriesUseCase().toCategoryItem().sortedBy { it.isEarning }
	}

	private suspend fun toOperationItem(list: List<Operation>): List<OperationItem> = viewModelScope.async(handler) {
		list.map {
			val account = getAccountByIdUseCase(it.account)
			val category = it.category?.let { id -> getCategoryByIdUseCase(id) }
			OperationItem(it.id, it.name, it.quantity, category, it.date, account.name)
		}.sortedByDescending { it.id }
	}.await()
}