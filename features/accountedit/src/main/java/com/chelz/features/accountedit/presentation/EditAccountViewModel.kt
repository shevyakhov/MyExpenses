package com.chelz.features.accountedit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.accountedit.presentation.navigation.EditAccountRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.SharedAccountItem
import com.chelz.shared.accounts.domain.entity.toAccount
import com.chelz.shared.accounts.domain.firebase.SharedAccountConstants
import com.chelz.shared.accounts.domain.usecase.account.DeleteAccountUseCase
import com.chelz.shared.accounts.domain.usecase.account.UpdateAccountUseCase
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditAccountViewModel(
	private val router: EditAccountRouter,
	private val deleteAccountUseCase: DeleteAccountUseCase,
	private val updateAccountUseCase: UpdateAccountUseCase,
) : ViewModel() {

	private val _error = MutableSharedFlow<String?>()
	val error: SharedFlow<String?> = _error

	companion object {

		const val DEFAULT_COLOR = "#000000"
	}

	private val app by lazy { Firebase.app }
	val auth by lazy { Firebase.auth }
	private val store by lazy { Firebase.firestore }

	private val sharedAccountsCollection = store.collection(SharedAccountConstants.SHARED_ACCOUNTS_TABLE)

	val chosenColorFlow = MutableStateFlow<String>(DEFAULT_COLOR)
	val hostFlow = MutableStateFlow("")
	val nameFlow = MutableStateFlow("")
	val currentAccountFlow = MutableStateFlow<AccountItem?>(null)
	val balanceFlow = MutableStateFlow(0.0)
	val cardNumberFlow = MutableStateFlow("")
	val isSharedFlow = MutableStateFlow(false)
	val usersEmails = MutableStateFlow(emptyList<String>())
	val guestFlow = MutableStateFlow(true)

	init {
		currentAccountFlow.filterNotNull().onEach {
			nameFlow.value = it.name
			balanceFlow.value = it.money
			chosenColorFlow.value = it.color
			cardNumberFlow.value = it.number
			if (it is SharedAccountItem) {
				isSharedFlow.value = true
				val account = sharedAccountsCollection.document(it.sharedId).get().await()
				val host = account.get(SharedAccountConstants.ACCOUNT.HOST_EMAIL).toString()
				val users = (account.get(SharedAccountConstants.ACCOUNT.USERS) as List<*>).map { any -> any.toString() }
				guestFlow.value = auth.currentUser?.email != host
				hostFlow.value = host
				usersEmails.value = users
			}
		}.launchIn(viewModelScope)
	}

	fun saveAccount() = viewModelScope.launch {
		if (!isSharedFlow.value) {
			val nameFlag = nameFlow.value.isNotBlank()
			if (!nameFlag) return@launch
			val account = Account(
				accountId = currentAccountFlow.value?.id ?: 0L,
				name = nameFlow.value,
				color = chosenColorFlow.value,
				number = cardNumberFlow.value,
				money = balanceFlow.value
			)
			updateAccountUseCase(account)
			navigateBack()
		} else {
			if (auth.currentUser?.email in usersEmails.value) {
				_error.emit("Вы не можете пригласить себя")
				return@launch
			}
			val account = (currentAccountFlow.value as SharedAccountItem).sharedId
			val accountData = hashMapOf(
				SharedAccountConstants.ACCOUNT.NAME to nameFlow.value,
				SharedAccountConstants.ACCOUNT.HOST_EMAIL to hostFlow.value,
				SharedAccountConstants.ACCOUNT.NUMBER to cardNumberFlow.value,
				SharedAccountConstants.ACCOUNT.COLOR to chosenColorFlow.value,
				SharedAccountConstants.ACCOUNT.MONEY to balanceFlow.value,
				SharedAccountConstants.ACCOUNT.USERS to usersEmails.value,
			)
			sharedAccountsCollection.document(account).update(accountData).addOnSuccessListener {
				navigateBack()
			}.addOnFailureListener {
				viewModelScope.launch {
					_error.emit(it.localizedMessage)
				}
			}
		}
	}

	fun deleteAccount() = viewModelScope.launch {
		if (!isSharedFlow.value) {
			currentAccountFlow.value?.toAccount()?.let { deleteAccountUseCase(it) }
			navigateBack()
		} else {
			val account = (currentAccountFlow.value as SharedAccountItem).sharedId
			sharedAccountsCollection.document(account).delete().addOnSuccessListener {
				navigateBack()
			}.addOnFailureListener {
				viewModelScope.launch {
					_error.emit(it.localizedMessage)
				}
			}
		}
	}

	private fun navigateBack() {
		router.navigateBack()
	}
}