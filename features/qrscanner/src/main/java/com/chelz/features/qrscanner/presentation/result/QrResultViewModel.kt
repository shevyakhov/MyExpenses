package com.chelz.features.qrscanner.presentation.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.qrscanner.domain.usecase.GetQrResultUseCase
import com.chelz.features.qrscanner.domain.usecase.GetTokenUseCase
import com.chelz.features.qrscanner.presentation.result.adapter.QrItem
import com.chelz.features.qrscanner.presentation.result.navigation.QrResultRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.usecase.account.GetAllAccountsUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetAllCategoriesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QrResultViewModel(
	private val router: QrResultRouter,
	private val getQrResultUseCase: GetQrResultUseCase,
	private val getTokenUseCase: GetTokenUseCase,
	private val getAllAccountsUseCase: GetAllAccountsUseCase,
	private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

	val chosenCategories = MutableStateFlow<List<QrItem>>(emptyList())
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

	companion object {

		val fakeCategory = Category(-1, "Ничего")
	}
}