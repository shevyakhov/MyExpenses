package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dao.OperationDao
import com.chelz.shared.accounts.domain.dto.OperationDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OperationDataSourceImpl(private val operationDao: OperationDao) : OperationDataSource {

	override suspend fun getAllOperations(): List<OperationDto> = withContext(Dispatchers.IO) {
		operationDao.getAllOperations()
	}

	override suspend fun getOperationById(id: Long): OperationDto = withContext(Dispatchers.IO) {
		operationDao.getOperationById(id)
	}

	override suspend fun getOperationByCategory(categoryId: Long): List<OperationDto> = withContext(Dispatchers.IO) {
		operationDao.getOperationByCategory(categoryId)
	}

	override suspend fun getOperationsByDate(date: Long): List<OperationDto> = withContext(Dispatchers.IO) {
		operationDao.getOperationsByDate(date)
	}

	override suspend fun insertOperation(operationDto: OperationDto): Long = withContext(Dispatchers.IO) {
		operationDao.insertOperation(operationDto)
	}

	override suspend fun updateOperation(operationDto: OperationDto) = withContext(Dispatchers.IO) {
		operationDao.updateOperation(operationDto)
	}

	override suspend fun deleteOperation(operationDto: OperationDto) = withContext(Dispatchers.IO) {
		operationDao.deleteOperation(operationDto)
	}

	override suspend fun clearOperations() = withContext(Dispatchers.IO) {
		operationDao.clearOperations()
	}
}