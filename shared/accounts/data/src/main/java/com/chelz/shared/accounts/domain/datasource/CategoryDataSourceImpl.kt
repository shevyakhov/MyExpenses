package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dao.CategoryDao
import com.chelz.shared.accounts.domain.dto.CategoryDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryDataSourceImpl(private val categoryDao: CategoryDao) : CategoryDataSource {

	override suspend fun getAllCategories(): List<CategoryDto> = withContext(Dispatchers.IO) {
		categoryDao.getAllCategories()
	}

	override suspend fun getCategoryById(id: Long): CategoryDto = withContext(Dispatchers.IO) {
		categoryDao.getCategoryById(id)
	}

	override suspend fun insertCategory(category: CategoryDto): Long = withContext(Dispatchers.IO) {
		categoryDao.insertCategory(category)
	}

	override suspend fun updateCategory(category: CategoryDto) = withContext(Dispatchers.IO) {
		categoryDao.updateCategory(category)
	}

	override suspend fun deleteCategory(category: CategoryDto) = withContext(Dispatchers.IO) {
		categoryDao.deleteCategory(category)
	}

	override suspend fun clearCategories() = withContext(Dispatchers.IO) {
		categoryDao.clearCategories()
	}
}