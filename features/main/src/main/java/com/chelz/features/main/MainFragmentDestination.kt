package com.chelz.features.main

import androidx.fragment.app.Fragment
import com.chelz.features.main.presentation.MainFragment
import com.chelz.libraries.navigation.FragmentDestination

object MainFragmentDestination : FragmentDestination {

	override fun createInstance(): Fragment = MainFragment()
}