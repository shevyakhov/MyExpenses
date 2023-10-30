package com.chelz.shared.accounts.domain.repository

import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.Operation

interface AccountRepository {

	suspend fun getAllAccounts(): List<Account>
	suspend fun getAccountById(id: Long): Account
	suspend fun insertAccount(account: Account): Long
	suspend fun updateAccount(account: Account)
	suspend fun deleteAccount(account: Account)

	suspend fun getAllOperations(): List<Operation>
	suspend fun getOperationById(id: Long): Operation
	suspend fun getOperationsByCategory(categoryId: Long): List<Operation>
	suspend fun getOperationsByDate(date: Long): List<Operation>
	suspend fun insertOperation(operation: Operation): Long
	suspend fun updateOperation(operation: Operation)
	suspend fun deleteOperation(operation: Operation)

	suspend fun getAllCategories(): List<Category>
	suspend fun getCategoryById(id: Long): Category
	suspend fun insertCategory(category: Category): Long
	suspend fun updateCategory(category: Category)
	suspend fun deleteCategory(category: Category)
}