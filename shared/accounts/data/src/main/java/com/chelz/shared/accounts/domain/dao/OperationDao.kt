package com.chelz.shared.accounts.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.chelz.shared.accounts.domain.dto.OperationDto

@Dao
interface OperationDao {

	@Query("SELECT * FROM Operations")
	suspend fun getAllOperations(): List<OperationDto>

	@Query("SELECT * FROM Operations WHERE id = :id")
	suspend fun getOperationById(id: Long): OperationDto

	@Query("SELECT * FROM Operations WHERE category = :categoryId")
	suspend fun getOperationByCategory(categoryId: Long): List<OperationDto>

	@Query("SELECT * FROM Operations WHERE date = :date")
	suspend fun getOperationsByDate(date: Long): List<OperationDto>

	@Transaction
	@Insert
	suspend fun insertOperation(operationDto: OperationDto): Long

	@Transaction
	@Update
	suspend fun updateOperation(operationDto: OperationDto)

	@Transaction
	@Delete
	suspend fun deleteOperation(operationDto: OperationDto)

	@Transaction
	@Query("DELETE FROM Operations")
	fun clearOperations()
}