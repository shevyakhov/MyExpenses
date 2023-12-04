package com.chelz.features.accountadd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.accountadd.presentation.navigation.AddAccountRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.firebase.SharedAccountConstants
import com.chelz.shared.accounts.domain.firebase.SharedAccountConstants.SHARED_ACCOUNTS_TABLE
import com.chelz.shared.accounts.domain.usecase.account.InsertAccountUseCase
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(
	private val router: AddAccountRouter,
	private val insertAccountsUseCase: InsertAccountUseCase,
) : ViewModel() {

	private val _error = MutableSharedFlow<String?>()
	val error: SharedFlow<String?> = _error

	companion object {

		const val DEFAULT_COLOR = "#000000"
	}

	private val app by lazy { Firebase.app }
	private val auth by lazy { Firebase.auth }
	private val store by lazy { Firebase.firestore }

	val sharedAccountsCollection = store.collection(SHARED_ACCOUNTS_TABLE)

	val chosenColorFlow = MutableStateFlow<String>(DEFAULT_COLOR)
	val nameFlow = MutableStateFlow("")
	val balanceFlow = MutableStateFlow(0.0)
	val cardNumberFlow = MutableStateFlow("")
	val isSharedFlow = MutableStateFlow(false)
	val usersEmails = MutableStateFlow(emptyList<String>())

	fun saveAccount() = viewModelScope.launch {
		if (!isSharedFlow.value) {
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
		} else {
			if (auth.currentUser?.email in usersEmails.value) {
				_error.emit("Вы не можете пригласить себя")
				return@launch
			}
		}
		val accountData = hashMapOf(
			SharedAccountConstants.ACCOUNT.NAME to nameFlow.value,
			SharedAccountConstants.ACCOUNT.HOST_EMAIL to auth.currentUser?.email,
			SharedAccountConstants.ACCOUNT.NUMBER to cardNumberFlow.value,
			SharedAccountConstants.ACCOUNT.COLOR to chosenColorFlow.value.toString(),
			SharedAccountConstants.ACCOUNT.MONEY to balanceFlow.value,
			SharedAccountConstants.ACCOUNT.USERS to usersEmails.value,
		)
		sharedAccountsCollection.add(accountData).addOnSuccessListener {
			navigateBack()
		}.addOnFailureListener {
			viewModelScope.launch {
				_error.emit(it.localizedMessage)
			}
		}
	}

	private fun navigateBack() {
		router.navigateBack()
	}
}