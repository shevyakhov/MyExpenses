package com.chelz.libraries.navigation

import com.chelz.libraries.navigation.Destination
import com.chelz.libraries.navigation.FragmentDestination

interface GlobalRouter {
	fun open(destination: Destination)

	fun replace(fragmentDestination: FragmentDestination)

	fun newRoot(fragmentDestination: FragmentDestination)

	fun popToRoot()

	fun exit()

	fun finish()

	fun popTo(fragmentDestinationClass: Class<out FragmentDestination>)
}