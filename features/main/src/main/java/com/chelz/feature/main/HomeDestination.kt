package com.chelz.feature.main

import androidx.fragment.app.Fragment
import com.chelz.feature.main.presentation.HomeFragment
import com.chelz.libraries.navigation.FragmentDestination

object HomeDestination : FragmentDestination {

	override fun createInstance(): Fragment =
		HomeFragment()
}