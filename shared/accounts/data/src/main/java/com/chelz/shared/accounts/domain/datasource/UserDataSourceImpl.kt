package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dao.UserDao
import com.chelz.shared.accounts.domain.dto.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataSourceImpl(private val userDao: UserDao) : UserDataSource {

	override suspend fun getAllUsers(): List<UserDto> = withContext(Dispatchers.IO) {
		userDao.getAllUsers()
	}

	override suspend fun getUserById(id: Long): UserDto? = withContext(Dispatchers.IO) {
		userDao.getUserById(id)
	}

	override suspend fun insertUser(user: UserDto) = withContext(Dispatchers.IO) {
		userDao.insertUser(user)
	}

	override suspend fun updateUser(user: UserDto) = withContext(Dispatchers.IO) {
		userDao.updateUser(user)
	}

	override suspend fun deleteUser(user: UserDto) = withContext(Dispatchers.IO) {
		userDao.deleteUser(user)
	}
}
