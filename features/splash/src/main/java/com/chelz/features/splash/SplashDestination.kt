package com.chelz.features.splash

import androidx.fragment.app.Fragment
import com.chelz.features.splash.presentation.SplashFragment
import com.chelz.libraries.navigation.FragmentDestination

class SplashDestination(private val firstStartFlag: Boolean = false) : FragmentDestination {

	override fun createInstance(): Fragment = SplashFragment.newInstance(firstStartFlag)
}