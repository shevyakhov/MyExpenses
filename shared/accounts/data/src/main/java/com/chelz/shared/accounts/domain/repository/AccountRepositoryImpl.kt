package com.chelz.shared.accounts.domain.repository

import com.chelz.shared.accounts.domain.datasource.AccountDataSource
import com.chelz.shared.accounts.domain.datasource.CategoryDataSource
import com.chelz.shared.accounts.domain.datasource.MonthGoalDataSource
import com.chelz.shared.accounts.domain.datasource.OperationDataSource
import com.chelz.shared.accounts.domain.datasource.UserDataSource
import com.chelz.shared.accounts.domain.entity.Account
import com.chelz.shared.accounts.domain.entity.AccountWithUsers
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.MonthGoal
import com.chelz.shared.accounts.domain.entity.Operation
import com.chelz.shared.accounts.domain.entity.User
import com.chelz.shared.accounts.domain.mapper.toDto
import com.chelz.shared.accounts.domain.mapper.toEntity
import com.chelz.shared.accounts.domain.mapper.toMonthGoalDto
import com.chelz.shared.accounts.domain.mapper.toMonthGoalEntities
import com.chelz.shared.accounts.domain.mapper.toMonthGoalEntity

class AccountRepositoryImpl(
	private val accountDataSource: AccountDataSource,
	private val operationDataSource: OperationDataSource,
	private val categoryDataSource: CategoryDataSource,
	private val userDataSource: UserDataSource,
	private val monthGoalDataSource: MonthGoalDataSource,
) : AccountRepository {

	override suspend fun getAllAccounts(): List<Account> =
		accountDataSource.getAllAccounts().map { it.toEntity() }

	override suspend fun getAccountById(id: Long): Account =
		accountDataSource.getAccountById(id).toEntity()

	override suspend fun insertAccount(account: Account): Long =
		accountDataSource.insertAccount(account.toDto())

	override suspend fun updateAccount(account: Account) =
		accountDataSource.updateAccount(account.toDto())

	override suspend fun deleteAccount(account: Account) =
		accountDataSource.deleteAccount(account.toDto())

	override suspend fun getAccountWithUsers(): List<AccountWithUsers> =
		accountDataSource.getAccountsWithUsers().toEntity()

	override suspend fun getAllOperations(): List<Operation> =
		operationDataSource.getAllOperations().map { it.toEntity() }

	override suspend fun getOperationById(id: Long): Operation =
		operationDataSource.getOperationById(id).toEntity()

	override suspend fun getOperationsByCategory(categoryId: Long): List<Operation> =
		operationDataSource.getOperationByCategory(categoryId).map { it.toEntity() }

	override suspend fun getOperationsByDate(date: Long): List<Operation> =
		operationDataSource.getOperationsByDate(date).map { it.toEntity() }

	override suspend fun insertOperation(operation: Operation): Long =
		operationDataSource.insertOperation(operation.toDto())

	override suspend fun updateOperation(operation: Operation) =
		operationDataSource.updateOperation(operation.toDto())

	override suspend fun deleteOperation(operation: Operation) =
		operationDataSource.deleteOperation(operation.toDto())

	override suspend fun getAllCategories(): List<Category> =
		categoryDataSource.getAllCategories().map { it.toEntity() }

	override suspend fun getCategoryById(id: Long): Category =
		categoryDataSource.getCategoryById(id).toEntity()

	override suspend fun insertCategory(category: Category): Long =
		categoryDataSource.insertCategory(category.toDto())

	override suspend fun updateCategory(category: Category) =
		categoryDataSource.updateCategory(category.toDto())

	override suspend fun deleteCategory(category: Category) =
		categoryDataSource.deleteCategory(category.toDto())

	override suspend fun getAllUsers(): List<User> =
		userDataSource.getAllUsers().map { User(it.userId, it.name, it.email) }

	override suspend fun getUserById(id: Long): User? =
		userDataSource.getUserById(id)?.toEntity()

	override suspend fun addUser(user: User) {
		userDataSource.insertUser(user.toDto())
	}

	override suspend fun updateUser(user: User) {
		userDataSource.updateUser(user.toDto())
	}

	override suspend fun insertMonthGoal(monthGoal: MonthGoal) {
		val monthGoalDto = monthGoal.toMonthGoalDto()
		monthGoalDataSource.insertMonthGoal(monthGoalDto)
	}

	override suspend fun getMonthGoalById(id: Long): MonthGoal? {
		val monthGoalDto = monthGoalDataSource.getMonthGoalById(id)
		return monthGoalDto?.toMonthGoalEntity()
	}

	override suspend fun getMonthGoalByAccountAndCategory(accountId: Long, categoryId: Long, yearMonth: String): MonthGoal? {
		val monthGoalDto = monthGoalDataSource.getMonthGoalByAccountAndCategory(accountId, categoryId, yearMonth)
		return monthGoalDto?.toMonthGoalEntity()
	}

	override suspend fun getAllMonthGoals(): List<MonthGoal> {
		val monthGoalDtos = monthGoalDataSource.getAllMonthGoals()
		return monthGoalDtos.toMonthGoalEntities()
	}

	override suspend fun updateMonthGoal(monthGoal: MonthGoal) {
		val monthGoalDto = monthGoal.toMonthGoalDto()
		monthGoalDataSource.updateMonthGoal(monthGoalDto)
	}

	override suspend fun deleteMonthGoalById(id: Long) {
		monthGoalDataSource.deleteMonthGoalById(id)
	}

	override suspend fun deleteUser(user: User) {
		userDataSource.deleteUser(user.toDto())
	}

	override suspend fun clearDatabase() {
		accountDataSource.clearAccounts()
		operationDataSource.clearOperations()
		categoryDataSource.clearCategories()
	}
}