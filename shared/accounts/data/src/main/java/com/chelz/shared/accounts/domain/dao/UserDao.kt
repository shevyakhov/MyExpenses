package com.chelz.shared.accounts.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chelz.shared.accounts.domain.dto.UserDto

@Dao
interface UserDao {

	@Query("SELECT * FROM user_table")
	suspend fun getAllUsers(): List<UserDto>

	@Query("SELECT * FROM user_table WHERE userId = :id")
	suspend fun getUserById(id: Long): UserDto?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertUser(user: UserDto)

	@Update
	suspend fun updateUser(user: UserDto)

	@Delete
	suspend fun deleteUser(user: UserDto)
}