package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dto.CategoryDto

interface CategoryDataSource {

	suspend fun getAllCategories(): List<CategoryDto>
	suspend fun getCategoryById(id: Long): CategoryDto
	suspend fun insertCategory(category: CategoryDto): Long
	suspend fun updateCategory(category: CategoryDto)
	suspend fun deleteCategory(category: CategoryDto)
	suspend fun clearCategories()
}