package com.chelz.features.qrscanner.presentation.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.qrscanner.presentation.result.adapter.QrItem
import com.chelz.features.qrscanner.presentation.result.adapter.toDto
import com.chelz.features.qrscanner.presentation.result.navigation.QrResultRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.usecase.account.GetAllAccountsUseCase
import com.chelz.shared.accounts.domain.usecase.account.UpdateAccountUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetAllCategoriesUseCase
import com.chelz.shared.accounts.domain.usecase.operation.InsertOperationUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QrResultViewModel(
	private val router: QrResultRouter,
	private val getAllAccountsUseCase: GetAllAccountsUseCase,
	private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
	private val insertOperationUseCase: InsertOperationUseCase,
	private val updateAccountUseCase: UpdateAccountUseCase,
) : ViewModel() {

	val chosenCategories = MutableStateFlow<List<QrItem>>(emptyList())
	val accountsFlow = MutableStateFlow<List<Account>>(listOf())
	val categoriesFlow = MutableStateFlow<List<Category>>(listOf())

	val currentAccount = MutableStateFlow<Account?>(null)

	private val handler = CoroutineExceptionHandler { _, throwable ->
		Log.e("HOMEVM", throwable.message.toString())
	}

	fun init() = viewModelScope.launch {
		updateAccounts()
		updateCategory()
	}

	private suspend fun updateAccounts() {
		accountsFlow.value = getAllAccountsUseCase()
	}

	private suspend fun updateCategory() {
		val category = getAllCategoriesUseCase().filter { !it.isEarning }.toMutableList()
		category.add(0, fakeCategory)
		categoriesFlow.value = category
	}

	fun initCategoriesSize(list: List<QrItem>) {
		chosenCategories.value = list
	}

	fun saveOperations(currentList: List<QrItem>) = viewModelScope.launch {
		val account = currentAccount.value ?: return@launch
		val prices = currentList.map { it.price }
		val totalSum = prices.sumOf { it!! / -100.0 }
		currentList.forEach {
			insertOperationUseCase(it.toDto(account))
		}
		account.money += totalSum
		updateAccountUseCase(account)
		router.navigateToHome()
	}

	companion object {

		val fakeCategory = Category(-1, "Ничего")
	}
}