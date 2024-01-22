package com.chelz.features.profile.presentation.adapter

import com.chelz.shared.accounts.domain.entity.CategoryItem

interface CategoryClickListener {

	fun onClick(categoryItem: CategoryItem?)
}