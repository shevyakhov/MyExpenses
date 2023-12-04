package com.chelz.features.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.home.domain.Numbers
import com.chelz.features.home.domain.SharedAccount
import com.chelz.features.home.domain.toAccountItem
import com.chelz.features.home.domain.usecase.GetTodaySpendScenario
import com.chelz.features.home.navigation.HomeRouter
import com.chelz.features.home.presentation.recycler.accounts.AccountItem
import com.chelz.features.home.presentation.recycler.accounts.toAccountItem
import com.chelz.features.home.presentation.recycler.operations.OperationItem
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.Operation
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
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.LocalDateTime
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
	private val router: HomeRouter,
) : ViewModel() {

	private val auth by lazy { Firebase.auth }
	private val store by lazy { Firebase.firestore }
	val operationItemFlow = MutableStateFlow<List<OperationItem>>(listOf())
	private val accountsFlow = MutableStateFlow<List<Account>>(listOf())
	val sharedAccountsFlow = MutableStateFlow<List<AccountItem>>(listOf())
	private val operationFlow = MutableStateFlow<List<Operation>>(listOf())
	val categoriesFlow = MutableStateFlow<List<Category>>(listOf())

	val currentAccount = MutableStateFlow<Account?>(null)
	private val currentCategory = MutableStateFlow<Category?>(null)
	private val sharedAccountsCollection = store.collection(SHARED_ACCOUNTS_TABLE)
	val stringFlow = MutableStateFlow("")
	val isEarningFlow = MutableStateFlow(false)

	val todaySpend = MutableStateFlow(0.0)

	private val handler = CoroutineExceptionHandler { _, throwable ->
		Log.e("HOMEVM", throwable.message.toString())
	}

	fun init() = viewModelScope.launch {
		updateAccounts()
		updateOperation()
		updateCategory()
	}

	private suspend fun updateAccounts() = viewModelScope.launch {
		accountsFlow.value = getAllAccountsUseCase()

		val userEmail = auth.currentUser?.email
		val local = accountsFlow.value.toAccountItem()
		val host: List<AccountItem> = getHostAccounts(userEmail).await()
		val shared: List<AccountItem> = getSharedAccounts(userEmail).await()
		val fullList = LinkedList<AccountItem>()
		fullList.addAll(local)
		fullList.addAll(host)
		fullList.addAll(shared)
		sharedAccountsFlow.emit(fullList)
	}

	private suspend fun getHostAccounts(userEmail: String?): Deferred<List<AccountItem>> = viewModelScope.async {
		val query = sharedAccountsCollection.whereEqualTo(
			SharedAccountConstants.ACCOUNT.HOST_EMAIL, userEmail.toString(),
		).get().await()

		query.documents.map {
			SharedAccount(
				accountId = it.id,
				name = it[SharedAccountConstants.ACCOUNT.NAME].toString(),
				number = it[SharedAccountConstants.ACCOUNT.NUMBER].toString(),
				color = it[SharedAccountConstants.ACCOUNT.COLOR].toString(),
				money = it[SharedAccountConstants.ACCOUNT.MONEY].toString().toDouble(),
				hostEmail = it[SharedAccountConstants.ACCOUNT.HOST_EMAIL].toString(),
			)
		}.toAccountItem()
	}

	private suspend fun getSharedAccounts(userEmail: String?): Deferred<List<AccountItem>> = viewModelScope.async {
		val query = sharedAccountsCollection.whereArrayContains(
			SharedAccountConstants.ACCOUNT.USERS, userEmail.toString(),
		).get().await()

		query.documents.map {
			SharedAccount(
				accountId = it.id,
				name = it[SharedAccountConstants.ACCOUNT.NAME].toString(),
				number = it[SharedAccountConstants.ACCOUNT.NUMBER].toString(),
				color = it[SharedAccountConstants.ACCOUNT.COLOR].toString(),
				money = it[SharedAccountConstants.ACCOUNT.MONEY].toString().toDouble(),
				hostEmail = it[SharedAccountConstants.ACCOUNT.HOST_EMAIL].toString(),
			)
		}.toAccountItem()

	}

	private suspend fun updateOperation() {
		operationFlow.value = getAllOperationsUseCase().sortedByDescending { it.date }
		todaySpend.value = getTodaySpendScenario.invoke()
		operationItemFlow.value = toOperationItem(operationFlow.value)
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

	fun onEnter() = viewModelScope.launch(handler) {
		if (stringFlow.value.isNotEmpty()) {
			insertLocalOperation()
		}
	}

	private suspend fun insertLocalOperation() {
		val quantity = stringFlow.value.toDouble() * if (isEarningFlow.value) 1 else -1
		val accountId = currentAccount.value?.accountId?.let { id ->
			val categoryId = currentCategory.value?.categoryId
			val categoryName = currentCategory.value?.name.toString()
			val operation = Operation(
				id = 0L,
				name = categoryName,
				quantity = quantity,
				account = id,
				category = categoryId,
				date = LocalDateTime.now().toDateTime().toString("yyyy-MM-dd")
			)

			insertOperationUseCase(operation)
			val account = currentAccount.value
			val newAccount = account?.copy()?.apply {
				this.money = this.money + quantity
			}
			updateAccountUseCase(newAccount ?: return)
			updateAccounts()
			updateOperation()
			currentAccount.value = newAccount
			stringFlow.value = ""
		}
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

	suspend fun toOperationItem(list: List<Operation>): List<OperationItem> = viewModelScope.async(handler) {
		list.map {
			val account = getAccountByIdUseCase(it.account)
			val category = it.category?.let { id -> getCategoryByIdUseCase(id) }
			OperationItem(it.id, it.name, it.quantity, category, it.date, account.name)
		}.sortedByDescending { it.id }
	}.await()

	fun navigateToAddAccount() {
		router.navigateToAddAccount()
	}

	fun navigateToAddCategory() {
		router.navigateToAddCategory()
	}
}
