package com.chelz.myexpenses.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.chelz.libraries.navigation.Destination
import com.chelz.libraries.navigation.FragmentDestination
import com.chelz.libraries.navigation.GlobalRouter
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

class GlobalRouterImpl(
	private val router: Router,
) : GlobalRouter {

	override fun open(destination: Destination) {
		router.navigateTo(
			CreateInstanceFragmentScreen(destination as FragmentDestination)
		)
	}

	override fun replace(fragmentDestination: FragmentDestination) {
		router.replaceScreen(
			CreateInstanceFragmentScreen(fragmentDestination)
		)
	}

	override fun newRoot(fragmentDestination: FragmentDestination) {
		router.newRootScreen(
			CreateInstanceFragmentScreen(fragmentDestination)
		)
	}

	override fun popToRoot() {
		router.backTo(null)
	}

	override fun exit() {
		router.exit()
	}

	override fun finish() {
		router.finishChain()
	}

	override fun popTo(fragmentDestinationClass: Class<out FragmentDestination>) {
		router.backTo(
			BackToFragmentScreen(fragmentDestinationClass.name)
		)
	}
}

private class CreateInstanceFragmentScreen(
	private val fragmentDestination: FragmentDestination,
) : FragmentScreen {

	override val screenKey: String = fragmentDestination::class.java.name

	override fun createFragment(factory: FragmentFactory): Fragment =
		fragmentDestination.createInstance()
}

private class BackToFragmentScreen(override val screenKey: String) : Screen