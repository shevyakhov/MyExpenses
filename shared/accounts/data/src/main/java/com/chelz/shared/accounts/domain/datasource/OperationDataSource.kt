package com.chelz.shared.accounts.domain.datasource

import com.chelz.shared.accounts.domain.dto.OperationDto

interface OperationDataSource {

	suspend fun getAllOperations(): List<OperationDto>
	suspend fun getOperationById(id: Long): OperationDto
	suspend fun getOperationByCategory(categoryId: Long): List<OperationDto>
	suspend fun getOperationsByDate(date: Long): List<OperationDto>
	suspend fun insertOperation(operationDto: OperationDto): Long
	suspend fun updateOperation(operationDto: OperationDto)
	suspend fun deleteOperation(operationDto: OperationDto)
}