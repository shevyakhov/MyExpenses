package com.chelz.features.categoryadd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.categoryadd.presentation.navigation.AddCategoryRouter
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.usecase.categories.InsertCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddCategoryViewModel(
	private val router: AddCategoryRouter,
	private val insertCategoryUseCase: InsertCategoryUseCase,
) : ViewModel() {

	companion object {

		const val DEFAULT_COLOR = "#000000"
	}

	val chosenColorFlow = MutableStateFlow(DEFAULT_COLOR)
	val nameFlow = MutableStateFlow("")
	val isEarningFlow = MutableStateFlow(false)

	fun saveCategory() = viewModelScope.launch {

		val nameFlag = nameFlow.value.isNotBlank()
		if (!nameFlag) return@launch
		val category = Category(
			categoryId = 0,
			name = nameFlow.value,
			color = chosenColorFlow.value,
			isEarning = isEarningFlow.value
		)

		insertCategoryUseCase(category)
		navigateBack()

	}

	private fun navigateBack() {
		router.navigateBack()
	}

}
