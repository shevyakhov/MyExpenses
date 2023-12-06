package com.chelz.features.home.presentation.recycler.operations

import com.chelz.shared.accounts.domain.entity.Category

open class OperationItem(
	val id: Long,
	open val name: String?,
	open val quantity: Double,
	open val category: Category?,
	open val date: String,
	open val account: String,
)
