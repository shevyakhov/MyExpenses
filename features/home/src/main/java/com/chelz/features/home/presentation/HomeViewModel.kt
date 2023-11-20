package com.chelz.features.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.home.domain.Numbers
import com.chelz.features.home.domain.usecase.GetTodaySpendScenario
import com.chelz.features.home.navigation.HomeRouter
import com.chelz.features.home.presentation.recycler.operations.OperationItem
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.usecase.account.GetAccountByIdUseCase
import com.chelz.shared.accounts.domain.usecase.account.GetAllAccountsUseCase
import com.chelz.shared.accounts.domain.usecase.account.UpdateAccountUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetAllCategoriesUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetCategoryByIdUseCase
import com.chelz.shared.accounts.domain.usecase.operation.GetAllOperationsUseCase
import com.chelz.shared.accounts.domain.usecase.operation.InsertOperationUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime

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

	val accountsFlow = MutableStateFlow<List<Account>>(listOf())
	val operationFlow = MutableStateFlow<List<Operation>>(listOf())
	val categoriesFlow = MutableStateFlow<List<Category>>(listOf())

	val currentAccount = MutableStateFlow<Account?>(null)
	val currentCategory = MutableStateFlow<Category?>(null)

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

	private suspend fun updateAccounts() {
		accountsFlow.value = getAllAccountsUseCase()
	}

	private suspend fun updateOperation() {
		operationFlow.value = getAllOperationsUseCase().sortedByDescending { it.date }
		todaySpend.value = getTodaySpendScenario.invoke()
		getAllOperationsUseCase.invoke().groupBy { it.date.drop(5).dropLast(3) }
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
				updateAccountUseCase(newAccount ?: return@launch)
				updateAccounts()
				updateOperation()
				currentAccount.value = newAccount
				stringFlow.value = ""
			}
		}
	}

	fun onQrClick() {

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
		}
	}.await()

}
