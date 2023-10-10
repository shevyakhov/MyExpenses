package com.chelz.libraries.navigation

import com.chelz.libraries.navigation.FragmentDestination
import kotlinx.coroutines.flow.StateFlow

interface MainRouter {

	val currentScreen: StateFlow<FragmentDestination>
	fun open(fragmentDestination: FragmentDestination)
	fun exit()
	fun clearBackStack()
}