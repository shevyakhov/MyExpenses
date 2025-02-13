package com.chelz.myexpenses.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.chelz.features.home.HomeDestination
import com.chelz.libraries.navigation.FragmentDestination
import com.chelz.libraries.navigation.MainRouter
import com.chelz.libraries.navigation.cicerone.Router
import com.chelz.libraries.navigation.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.flow.MutableStateFlow

class MainRouterImpl(
	private val router: Router,
) : MainRouter {

	override val currentScreen = MutableStateFlow<FragmentDestination>(HomeDestination)

	private val backStack = mutableListOf<FragmentDestination>()

	override fun open(fragmentDestination: FragmentDestination) {
		backStack.remove(fragmentDestination)
		backStack.add(fragmentDestination)
		currentScreen.value = fragmentDestination
		router.navigateTo(
			CreateInstanceFragmentMainScreen(fragmentDestination)
		)
	}

	override fun exit() {
		backStack.removeAt(backStack.lastIndex)
		if (backStack.isNotEmpty()) {
			open(backStack.last())
		} else {
			router.finishChain()
		}
	}

	override fun clearBackStack() {
		backStack.clear()
	}
}

private class CreateInstanceFragmentMainScreen(
	private val fragmentDestination: FragmentDestination,
) : FragmentScreen {

	override val screenKey: String = fragmentDestination::class.java.name

	override fun createFragment(factory: FragmentFactory): Fragment = fragmentDestination.createInstance()
}