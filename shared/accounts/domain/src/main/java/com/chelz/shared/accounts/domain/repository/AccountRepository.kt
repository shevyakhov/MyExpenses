package com.chelz.shared.accounts.domain.repository

import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountWithUsers
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.MonthGoal
import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.entity.User

interface AccountRepository {

	suspend fun getAllAccounts(): List<Account>
	suspend fun getAccountById(id: Long): Account
	suspend fun insertAccount(account: Account): Long
	suspend fun updateAccount(account: Account)
	suspend fun deleteAccount(account: Account)

	suspend fun getAccountWithUsers(): List<AccountWithUsers>

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

	suspend fun getAllUsers(): List<User>
	suspend fun getUserById(id: Long): User?
	suspend fun addUser(user: User)
	suspend fun updateUser(user: User)
	suspend fun deleteUser(user: User)

	suspend fun insertMonthGoal(monthGoal: MonthGoal)
	suspend fun getMonthGoalById(id: Long): MonthGoal?
	suspend fun getMonthGoalByAccountAndCategory(accountId: Long, categoryId: Long, yearMonth: String): MonthGoal?
	suspend fun getAllMonthGoals(): List<MonthGoal>
	suspend fun updateMonthGoal(monthGoal: MonthGoal)
	suspend fun deleteMonthGoalById(id: Long)

	suspend fun clearDatabase()
}