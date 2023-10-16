package com.chelz.features.main.presentation.navigation

import com.chelz.features.main.MainScreenValue
import kotlinx.coroutines.flow.Flow

interface MainFragmentRouter {

	val screenFlow: Flow<MainScreenValue>

	fun navigateToFoodList()

	fun navigateToRecipes()

	fun navigateToBuyList()

	fun navigateToProfile()

	fun goBack()
}