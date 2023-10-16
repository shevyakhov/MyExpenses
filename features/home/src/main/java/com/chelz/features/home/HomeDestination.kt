package com.chelz.features.home

import androidx.fragment.app.Fragment
import com.chelz.features.home.presentation.HomeFragment
import com.chelz.libraries.navigation.FragmentDestination

object HomeDestination : FragmentDestination {

	override fun createInstance(): Fragment = HomeFragment()
}