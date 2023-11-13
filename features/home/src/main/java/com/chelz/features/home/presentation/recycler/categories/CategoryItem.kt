package com.chelz.features.home.presentation.recycler.categories

import com.chelz.shared.accounts.domain.entity.Category

class CategoryItem(
	val categoryId: Long,
	var name: String,
	var isEarning: Boolean = false,
	var color: String = "#FF0000",
)

internal fun CategoryItem.toCategory() =
	Category(categoryId, name, isEarning, color)

internal fun List<Category>.toCategoryItem(): List<CategoryItem> =
	map { CategoryItem(it.categoryId, it.name, it.isEarning, it.color) }