package com.chelz.features.qrscanner.presentation.result

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.qrscanner.presentation.result.adapter.QrItem
import com.chelz.features.qrscanner.presentation.result.adapter.toAccount
import com.chelz.features.qrscanner.presentation.result.adapter.toDto
import com.chelz.features.qrscanner.presentation.result.navigation.QrResultRouter
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.SharedAccount
import com.chelz.shared.accounts.domain.entity.SharedAccountItem
import com.chelz.shared.accounts.domain.entity.SharedCategory
import com.chelz.shared.accounts.domain.entity.SharedOperation
import com.chelz.shared.accounts.domain.entity.toAccountItem
import com.chelz.shared.accounts.domain.entity.toOperationItem
import com.chelz.shared.accounts.domain.firebase.SharedAccountConstants
import com.chelz.shared.accounts.domain.usecase.account.GetAllAccountsUseCase
import com.chelz.shared.accounts.domain.usecase.account.UpdateAccountUseCase
import com.chelz.shared.accounts.domain.usecase.categories.GetAllCategoriesUseCase
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
import java.util.LinkedList

class QrResultViewModel(
	private val router: QrResultRouter,
	private val getAllAccountsUseCase: GetAllAccountsUseCase,
	private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
	private val insertOperationUseCase: InsertOperationUseCase,
	private val updateAccountUseCase: UpdateAccountUseCase,
) : ViewModel() {

	val chosenCategories = MutableStateFlow<List<QrItem>>(emptyList())

	private val auth by lazy { Firebase.auth }
	private val store by lazy { Firebase.firestore }
	private val sharedAccountsCollection = store.collection(SharedAccountConstants.SHARED_ACCOUNTS_TABLE)
	private val accountsFlow = MutableStateFlow<List<Account>>(listOf())
	val fullAccountsFlow = MutableStateFlow<List<AccountItem>>(listOf())
	val categoriesFlow = MutableStateFlow<List<Category>>(listOf())

	val currentAccount = MutableStateFlow<AccountItem?>(null)

	private val handler = CoroutineExceptionHandler { _, throwable ->
		Log.e("HOMEVM", throwable.message.toString())
	}

	fun init() = viewModelScope.launch {
		updateAccounts().await()
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
			}
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
		val category = getAllCategoriesUseCase().filter { !it.isEarning }.toMutableList()
		category.add(0, fakeCategory)
		categoriesFlow.value = category
	}

	fun initCategoriesSize(list: List<QrItem>) {
		chosenCategories.value = list
	}

	fun saveOperations(currentList: List<QrItem>) = viewModelScope.launch {
		val isLocal = currentAccount.value?.id.takeIf { (it?.toInt() ?: -1) > 0 } != null
		if (isLocal) {
			insertLocalOperations(currentList)
		} else {
			insertRemoteOperations(currentList)
		}
	}

	private fun insertRemoteOperations(currentList: List<QrItem>) = viewModelScope.launch {
		val account = currentAccount.value as? SharedAccountItem ?: return@launch
		val documentId = account.sharedId
		val prices = currentList.map { it.price }
		val totalSum = prices.sumOf { it!! / -100.0 }

		currentList.forEach {
			val category = SharedCategory(
				it.category?.name ?: "?",
				it.category?.isEarning ?: false,
				it.category?.color ?: "#FFFCDE",
			)

			val operation = SharedOperation(
				name = it.name ?: it.category?.name ?: "?",
				quantity = it.price!! / -100.0,
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
		}

		sharedAccountsCollection.document(documentId)
			.update(SharedAccountConstants.ACCOUNT.MONEY, FieldValue.increment(totalSum)).await()

		router.navigateToHome()
	}

	private fun insertLocalOperations(currentList: List<QrItem>) = viewModelScope.launch {
		val account = currentAccount.value ?: return@launch
		val prices = currentList.map { it.price }
		val totalSum = prices.sumOf { it!! / -100.0 }
		currentList.forEach {
			insertOperationUseCase(it.toDto(account.toAccount()))
		}
		account.money += totalSum
		updateAccountUseCase(account.toAccount())
		router.navigateToHome()
	}

	private fun List<Account>.toAccountItem(): List<AccountItem> =
		map { AccountItem(it.accountId, it.name, it.number, it.color, it.money) }

	companion object {

		val fakeCategory = Category(-1, "Ничего")
	}
}