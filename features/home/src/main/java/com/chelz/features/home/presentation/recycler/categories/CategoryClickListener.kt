package com.chelz.features.home.presentation.recycler.categories

import com.chelz.shared.accounts.domain.entity.CategoryItem

interface CategoryClickListener {

	fun onClick(categoryItem: CategoryItem?)
}