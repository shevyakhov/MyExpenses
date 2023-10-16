package com.chelz.features.profile

import androidx.fragment.app.Fragment
import com.chelz.features.profile.presentation.ProfileFragment
import com.chelz.libraries.navigation.FragmentDestination

object ProfileDestination : FragmentDestination {

	override fun createInstance(): Fragment = ProfileFragment()
}