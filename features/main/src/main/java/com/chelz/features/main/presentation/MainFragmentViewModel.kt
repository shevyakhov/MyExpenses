package com.chelz.features.main.presentation

import androidx.lifecycle.ViewModel
import com.chelz.features.main.MainScreenValue
import com.chelz.features.main.presentation.navigation.MainFragmentRouter
import kotlinx.coroutines.flow.Flow

class MainFragmentViewModel(
	private val navigator: MainFragmentRouter,
) : ViewModel() {

	val mainScreenValueFlow: Flow<MainScreenValue> = navigator.screenFlow

	init {
		navigator.navigateToFoodList()
	}

	fun navigateBack() {
		navigator.goBack()
	}

	fun navigateToFoodList() {
		navigator.navigateToFoodList()
	}

	fun navigateToRecipes() {
		navigator.navigateToRecipes()
	}

	fun navigateToToBuyList() {
		navigator.navigateToBuyList()
	}

	fun navigateToProfile() {
		navigator.navigateToProfile()
	}
}