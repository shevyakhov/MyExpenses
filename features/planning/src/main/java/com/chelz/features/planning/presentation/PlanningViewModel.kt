package com.chelz.features.planning.presentation

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.planning.presentation.navigation.PlanningRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.MonthGoal
import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.entity.OperationItem
import com.chelz.shared.accounts.domain.entity.SharedAccount
import com.chelz.shared.accounts.domain.entity.SharedAccountItem
import com.chelz.shared.accounts.domain.entity.SharedCategory
import com.chelz.shared.accounts.domain.entity.SharedOperation
import com.chelz.shared.accounts.domain.entity.toAccountItem
import com.chelz.shared.accounts.domain.entity.toOperationItem
import com.chelz.shared.accounts.domain.firebase.SharedAccountConstants
import com.chelz.shared.accounts.domain.usecase.ClearDataBaseUseCase
import com.chelz.shared.accounts.domain.usecase.DeleteMonthGoalByIdUseCase
import com.chelz.shared.accounts.domain.usecase.GetAllMonthGoalsUseCase
import com.chelz.shared.accounts.domain.usecase.GetMonthGoalByAccountAndCategoryUseCase
import com.chelz.shared.accounts.domain.usecase.GetMonthGoalByIdUseCase
import com.chelz.shared.accounts.domain.usecase.InsertMonthGoalUseCase
import com.chelz.shared.accounts.domain.usecase.UpdateMonthGoalUseCase
import com.chelz.shared.accounts.domain.usecase.account.GetAccountByIdUseCase
import com.chelz.shared.accounts.domain.usecase.account.GetAllAccountsUseCase
import com.chelz.shared.accounts.domain.usecase.categories.DeleteCategoryUseCase
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.util.LinkedList

class PlanningViewModel(
	private val getAllAccountsUseCase: GetAllAccountsUseCase,
	private val getAllOperationsUseCase: GetAllOperationsUseCase,
	private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
	private val getAccountByIdUseCase: GetAccountByIdUseCase,
	private val deleteCategoryUseCase: DeleteCategoryUseCase,
	private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
	private val clearDataBaseUseCase: ClearDataBaseUseCase,
	private val insertMonthGoalUseCase: InsertMonthGoalUseCase,
	private val getMonthGoalByIdUseCase: GetMonthGoalByIdUseCase,
	private val getMonthGoalByAccountAndCategoryUseCase: GetMonthGoalByAccountAndCategoryUseCase,
	private val getAllMonthGoalsUseCase: GetAllMonthGoalsUseCase,
	private val updateMonthGoalUseCase: UpdateMonthGoalUseCase,
	private val deleteMonthGoalByIdUseCase: DeleteMonthGoalByIdUseCase,
	private val router: PlanningRouter,
) : ViewModel() {

	val currentGoal = MutableStateFlow<MonthGoal?>(null)

	val auth by lazy { Firebase.auth }
	private val store by lazy { Firebase.firestore }

	private val accountsFlow = MutableStateFlow<List<Account>>(listOf())
	val fullAccountsFlow = MutableStateFlow<List<AccountItem>>(listOf())
	private val operationFlow = MutableStateFlow<List<Operation>>(listOf())
	val sharedOperationFlow = MutableStateFlow<List<OperationItem>>(listOf())
	val categoriesFlow = MutableStateFlow<List<Category>>(listOf())
	private val _allMonthGoals = MutableStateFlow<List<MonthGoal>>(emptyList())
	val allMonthGoals: StateFlow<List<MonthGoal>> get() = _allMonthGoals

	val currentAccount = MutableStateFlow<AccountItem?>(null)
	val currentCategory = MutableStateFlow<Category?>(null)
	val currentCalendar = MutableStateFlow<String>(DateTime.now().toString("MM-YYYY"))

	private val sharedAccountsCollection = store.collection(SharedAccountConstants.SHARED_ACCOUNTS_TABLE)

	fun convertTimestampToFormattedDate(timestamp: Long?): String {
		if (timestamp == null) return ""
		val dateTime = DateTime(timestamp)
		return DateTimeFormat.forPattern("dd.MM.yyyy").print(dateTime)
	}

	private val handler = CoroutineExceptionHandler { _, throwable ->
		Log.e("HOMEVM", throwable.message.toString())
	}

	fun init() = viewModelScope.launch {
		getAllMonthGoals()
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
	}

	fun getAllMonthGoals() {
		viewModelScope.launch {
			_allMonthGoals.value = getAllMonthGoalsUseCase()
		}
	}

	fun searchMonthGoalsByDate(date: String): List<MonthGoal> {
		return _allMonthGoals.value.filter { it.yearMonth == date }
	}

	private suspend fun updateOperation() = viewModelScope.async {
		operationFlow.value = getAllOperationsUseCase()
		val localOperations = toOperationItem(operationFlow.value).toMutableList()
		val sharedAccounts = fullAccountsFlow.value.filterIsInstance<SharedAccountItem>().map { it.operations.toOperationItem() }.flatten()
		localOperations.addAll(sharedAccounts)

		sharedOperationFlow.value = localOperations.sortedByDescending {
			LocalDateTime.parse(it.date, DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss.SSS"))
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
		categoriesFlow.value = getAllCategoriesUseCase()
	}

	private suspend fun toOperationItem(list: List<Operation>): List<OperationItem> = viewModelScope.async(handler) {
		list.map {
			val account = getAccountByIdUseCase(it.account)
			val category = it.category?.let { id -> getCategoryByIdUseCase(id) }
			OperationItem(it.id, it.name, it.quantity, category, it.date, account.name)
		}.sortedByDescending { it.id }
	}.await()

	fun insertGoal(goal: Pair<String, String>) {
		viewModelScope.launch {
			val monthGoal = MonthGoal(
				id = 0,
				goal.first,
				currentAccount.value?.name ?: return@launch,
				currentCategory.value?.name.toString(),
				monthlyLimit = goal.second.toDouble(),
				yearMonth = currentCalendar.value
			)
			insertMonthGoalUseCase.invoke(monthGoal)
			getAllMonthGoals()
		}
	}

	fun deleteGoal(goal: MonthGoal) {
		viewModelScope.launch {
			deleteMonthGoalByIdUseCase.invoke(goal.id)
			getAllMonthGoals()
		}
	}
}