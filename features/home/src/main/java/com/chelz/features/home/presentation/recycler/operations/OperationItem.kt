package com.chelz.features.home.presentation.recycler.operations

import com.chelz.shared.accounts.domain.entity.Category

data class OperationItem(
	val id: Long,
	val name: String,
	val quantity: Double,
	val category: Category?,
	val date: String,
	val account: String,
)
