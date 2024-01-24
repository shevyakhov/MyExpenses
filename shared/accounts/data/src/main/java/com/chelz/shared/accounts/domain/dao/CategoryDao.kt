package com.chelz.shared.accounts.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.chelz.shared.accounts.domain.dto.CategoryDto

@Dao
interface CategoryDao {

	@Query("SELECT * FROM Categories")
	suspend fun getAllCategories(): List<CategoryDto>

	@Query("SELECT * FROM Categories WHERE categoryId = :id")
	suspend fun getCategoryById(id: Long): CategoryDto

	@Transaction
	@Insert
	suspend fun insertCategory(categoryDto: CategoryDto): Long

	@Transaction
	@Update
	suspend fun updateCategory(categoryDto: CategoryDto)

	@Transaction
	@Delete
	suspend fun deleteCategory(categoryDto: CategoryDto)

	@Transaction
	@Query("DELETE FROM Categories")
	fun clearCategories()
}