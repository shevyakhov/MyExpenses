package com.chelz.features.accountadd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.accountadd.presentation.navigation.AddAccountRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.usecase.account.InsertAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(
	private val router: AddAccountRouter,
	private val insertAccountsUseCase: InsertAccountUseCase,
) : ViewModel() {

	companion object {

		const val DEFAULT_COLOR = "#000000"
	}

	val chosenColorFlow = MutableStateFlow<String>(DEFAULT_COLOR)
	val nameFlow = MutableStateFlow("")
	val balanceFlow = MutableStateFlow(0.0)
	val cardNumberFlow = MutableStateFlow("")
	val isSharedFlow = MutableStateFlow(false)

	fun saveAccount() = viewModelScope.launch {
		val nameFlag = nameFlow.value.isNotBlank()
		if (!nameFlag) return@launch
		val account = Account(
			accountId = 0,
			name = nameFlow.value,
			color = chosenColorFlow.value,
			number = cardNumberFlow.value,
			money = balanceFlow.value
		)
		insertAccountsUseCase(account)
		navigateBack()
	}

	private fun navigateBack() {
		router.navigateBack()
	}
}