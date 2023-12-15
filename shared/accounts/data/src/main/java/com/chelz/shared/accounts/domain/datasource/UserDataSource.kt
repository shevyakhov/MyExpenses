package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dto.UserDto

interface UserDataSource {

	suspend fun getAllUsers(): List<UserDto>
	suspend fun getUserById(id: Long): UserDto?
	suspend fun insertUser(user: UserDto)
	suspend fun updateUser(user: UserDto)
	suspend fun deleteUser(user: UserDto)
}
