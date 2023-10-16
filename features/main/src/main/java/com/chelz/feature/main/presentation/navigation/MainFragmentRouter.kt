package com.chelz.feature.main.presentation.navigation

import com.chelz.feature.main.MainScreenValue
import kotlinx.coroutines.flow.Flow

interface MainFragmentRouter {

	val screenFlow: Flow<MainScreenValue>

	fun navigateToFoodList()

	fun navigateToRecipes()

	fun navigateToBuyList()

	fun navigateToProfile()

	fun goBack()
}