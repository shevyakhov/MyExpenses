package com.chelz.features.home.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.home.domain.Numbers
import com.chelz.features.home.domain.usecase.GetTodaySpendScenario
import com.chelz.features.home.domain.usecase.GetWeeklySpendUseCase
import com.chelz.features.home.navigation.HomeRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.entity.OperationItem
import com.chelz.shared.accounts.domain.entity.SharedAccount
import com.chelz.shared.accounts.domain.entity.SharedAccountItem
import com.chelz.shared.accounts.domain.entity.SharedCategory
import com.chelz.shared.accounts.domain.entity.SharedOperation
import com.chelz.shared.accounts.domain.entity.toAccount
import com.chelz.shared.accounts.domain.entity.toAccountItem
import com.chelz.shared.accounts.domain.entity.toOperationItem
import com.chelz.shared.accounts.domain.firebase.SharedAccountConstants
import com.chelz.shared.accounts.domain.firebase.SharedAccountConstants.SHARED_ACCOUNTS_TABLE
import com.chelz.shared.accounts.domain.usecase.account.GetAccountByIdUseCase
import com.chelz.shared.accounts.domain.usecase.account.GetAllAccountsUseCase
import com.chelz.shared.accounts.domain.usecase.account.UpdateAccountUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetAllCategoriesUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetCategoryByIdUseCase
import com.chelz.shared.accounts.domain.usecase.operation.GetAllOperationsUseCase
import com.chelz.shared.accounts.domain.usecase.operation.InsertOperationUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.util.LinkedList

class HomeViewModel(
	private val getAllAccountsUseCase: GetAllAccountsUseCase,
	private val getAllOperationsUseCase: GetAllOperationsUseCase,
	private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
	private val insertOperationUseCase: InsertOperationUseCase,
	private val updateAccountUseCase: UpdateAccountUseCase,
	private val getAccountByIdUseCase: GetAccountByIdUseCase,
	private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
	private val getTodaySpendScenario: GetTodaySpendScenario,
	private val getWeeklySpendUseCase: GetWeeklySpendUseCase,
	private val router: HomeRouter,
) : ViewModel() {

	private val auth by lazy { Firebase.auth }
	private val store by lazy { Firebase.firestore }
	private val accountsFlow = MutableStateFlow<List<Account>>(listOf())
	val fullAccountsFlow = MutableStateFlow<List<AccountItem>>(listOf())
	private val operationFlow = MutableStateFlow<List<Operation>>(listOf())
	val sharedOperationFlow = MutableStateFlow<List<OperationItem>>(listOf())
	val categoriesFlow = MutableStateFlow<List<Category>>(listOf())

	val currentAccount = MutableStateFlow<AccountItem?>(null)
	private val currentCategory = MutableStateFlow<Category?>(null)
	private val sharedAccountsCollection = store.collection(SHARED_ACCOUNTS_TABLE)
	val stringFlow = MutableStateFlow("")
	val isEarningFlow = MutableStateFlow(false)

	val todaySpend = MutableStateFlow(0.0)
	val weekSpend = MutableStateFlow<Map<Int, List<OperationItem>>>(mapOf())

	private val handler = CoroutineExceptionHandler { _, throwable ->
		Log.e("HOMEVM", throwable.message.toString())
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
				Log.w(TAG, "Listen failed.", error)
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
				Log.w(TAG, "Listen failed.", error)
				return@addSnapshotListener
			}
			viewModelScope.launch {
				updateAccounts().await()
				updateOperation().await()
			}
		}
	}

	private suspend fun updateOperation() = viewModelScope.async {
		operationFlow.value = getAllOperationsUseCase()
		val localOperations = toOperationItem(operationFlow.value).toMutableList()
		val sharedAccounts = fullAccountsFlow.value.filterIsInstance<SharedAccountItem>().map { it.operations.toOperationItem() }.flatten()
		localOperations.addAll(sharedAccounts)

		sharedOperationFlow.value = localOperations.sortedByDescending {
			LocalDateTime.parse(it.date, DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss.SSS"))
		}
		todaySpend.value = getTodaySpendScenario.invoke(sharedOperationFlow.value) // remote
		weekSpend.value = getWeeklySpendUseCase.invoke(sharedOperationFlow.value)
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

	fun onEnter() = viewModelScope.launch(handler) {
		if (stringFlow.value.isNotEmpty()) {
			val isLocal = currentAccount.value?.id.takeIf { (it?.toInt() ?: -1) > 0 } != null
			if (isLocal) {
				insertLocalOperation()
			} else {
				insertRemoteOperation()
			}
			updateAccounts().await()
			updateOperation().await()
			stringFlow.value = ""
		}
	}

	private suspend fun insertLocalOperation() {
		val quantity = stringFlow.value.toDouble() * if (isEarningFlow.value) 1 else -1
		currentAccount.value?.toAccount()?.accountId?.let { id ->
			val categoryId = currentCategory.value?.categoryId
			val categoryName = currentCategory.value?.name
			val operation = Operation(
				id = 0L,
				name = categoryName,
				quantity = quantity,
				account = id,
				category = categoryId,
				date = LocalDateTime.now().toDateTime().toString("dd-MM-yyyy HH:mm:ss.SSS")
			)

			insertOperationUseCase(operation)
			val account = currentAccount.value?.toAccount()
			val newAccount = account?.copy()?.apply {
				this.money = this.money + quantity
			}
			updateAccountUseCase(newAccount ?: return)
			currentAccount.value = newAccount.toAccountItem()
		}
	}

	private fun insertRemoteOperation() = viewModelScope.launch {
		val quantity = stringFlow.value.toDouble() * if (isEarningFlow.value) 1 else -1
		val account = currentAccount.value as? SharedAccountItem ?: return@launch
		val documentId = account.sharedId

		val category = SharedCategory(
			currentCategory.value?.name ?: "?",
			currentCategory.value?.isEarning ?: false,
			currentCategory.value?.color ?: "#FFFCDE",
		)
		val categoryName = currentCategory.value?.name.toString()
		val operation = SharedOperation(
			name = categoryName,
			quantity = quantity,
			account = buildString {
				append(account.name)
				append(" ")
				append(auth.currentUser?.email.toString())
			},
			category = category,
			date = LocalDateTime.now().toDateTime().toString("dd-MM-yyyy HH:mm:ss.SSS")
		)

		sharedAccountsCollection.document(documentId)
			.update(SharedAccountConstants.ACCOUNT.OPERATIONS, FieldValue.arrayUnion(operation)).await()

		sharedAccountsCollection.document(documentId)
			.update(SharedAccountConstants.ACCOUNT.MONEY, FieldValue.increment(quantity)).await()

		currentAccount.value = account
	}

	private suspend fun updateCategory() {
		categoriesFlow.value = getAllCategoriesUseCase()
	}

	fun addZero() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Zero.value
		}
	}

	fun addOne() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.One.value
		} else
			stringFlow.value = Numbers.One.value
	}

	fun addTwo() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Two.value
		} else
			stringFlow.value = Numbers.Two.value
	}

	fun addThree() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Three.value
		} else
			stringFlow.value = Numbers.Three.value
	}

	fun addFour() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Four.value
		} else
			stringFlow.value = Numbers.Four.value
	}

	fun addFive() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Five.value
		} else
			stringFlow.value = Numbers.Five.value
	}

	fun addSix() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Six.value
		} else
			stringFlow.value = Numbers.Six.value
	}

	fun addSeven() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Seven.value
		} else
			stringFlow.value = Numbers.Seven.value
	}

	fun addEight() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Eight.value
		} else
			stringFlow.value = Numbers.Eight.value
	}

	fun addNine() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value + Numbers.Nine.value
		} else
			stringFlow.value = Numbers.Nine.value
	}

	fun addDot() {
		if (!stringFlow.value.contains(".")) {
			if (stringFlow.value.isNotEmpty()) {
				stringFlow.value = stringFlow.value + Numbers.Dot.value
			} else
				stringFlow.value = Numbers.Zero.value + Numbers.Dot.value
		}
	}

	fun delete() {
		if (stringFlow.value.isNotEmpty()) {
			stringFlow.value = stringFlow.value.dropLast(1)
		}
	}

	fun erase() {
		stringFlow.value = ""
	}

	fun onQrClick() {
		router.navigateToQrScanner()
	}

	fun onReverseClick() {
		isEarningFlow.value = !isEarningFlow.value
		currentCategory.value = null
	}

	fun onItemClick(category: Category?) {
		currentCategory.value = category
	}

	private suspend fun toOperationItem(list: List<Operation>): List<OperationItem> = viewModelScope.async(handler) {
		list.map {
			val account = getAccountByIdUseCase(it.account)
			val category = it.category?.let { id -> getCategoryByIdUseCase(id) }
			OperationItem(it.id, it.name, it.quantity, category, it.date, account.name)
		}.sortedByDescending { it.id }
	}.await()

	private fun Account.toAccountItem(): AccountItem {
		return AccountItem(
			accountId, name, number, color, money
		)
	}

	fun navigateToAddAccount() {
		router.navigateToAddAccount()
	}

	fun navigateToAddCategory() {
		router.navigateToAddCategory()
	}

	fun navigateToEditAccount(accountItem: AccountItem) {
		router.navigateToEditAccount(accountItem)
	}
}
