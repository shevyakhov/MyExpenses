package com.chelz.myexpenses.di

import com.chelz.features.home.HomeDestination
import com.chelz.features.main.MainScreenValue
import com.chelz.features.main.presentation.navigation.MainFragmentRouter
import com.chelz.features.planning.PlanningDestination
import com.chelz.features.profile.ProfileDestination
import com.chelz.features.statistics.StatisticsDestination
import com.chelz.libraries.navigation.MainRouter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainFragmentRouterImpl(private val router: MainRouter) : MainFragmentRouter {

	override val screenFlow: Flow<MainScreenValue> = router.currentScreen.map {
		when (it) {
			HomeDestination       -> MainScreenValue.Home
			StatisticsDestination -> MainScreenValue.Statistics
			PlanningDestination   -> MainScreenValue.Planning
			ProfileDestination    -> MainScreenValue.Profile
			else                  -> throw Error("cannot handle ${it::class.simpleName}")
		}
	}

	override fun navigateToFoodList() {
		router.open(HomeDestination)
	}

	override fun navigateToRecipes() {
		router.open(StatisticsDestination)
	}

	override fun navigateToBuyList() {
		router.open(PlanningDestination)
	}

	override fun navigateToProfile() {
		router.open(ProfileDestination)
	}

	override fun goBack() {
		router.exit()
	}
}