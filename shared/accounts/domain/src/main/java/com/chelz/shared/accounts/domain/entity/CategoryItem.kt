package com.chelz.shared.accounts.domain.entity

class CategoryItem(
	val categoryId: Long,
	var name: String,
	var isEarning: Boolean = false,
	var color: String = "#FF0000",
)

fun CategoryItem.toCategory() =
	Category(categoryId, name, isEarning, color)

fun List<Category>.toCategoryItem(): List<CategoryItem> =
	map { CategoryItem(it.categoryId, it.name, it.isEarning, it.color) }