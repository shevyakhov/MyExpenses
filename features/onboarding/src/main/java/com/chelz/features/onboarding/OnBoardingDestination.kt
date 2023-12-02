package com.chelz.features.onboarding

import androidx.fragment.app.Fragment
import com.chelz.features.onboarding.presentation.OnBoardingFragment
import com.chelz.libraries.navigation.FragmentDestination

object OnBoardingDestination : FragmentDestination {

	override fun createInstance(): Fragment =
		OnBoardingFragment()
}