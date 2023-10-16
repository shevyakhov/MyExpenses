package com.chelz.feature.main

import androidx.fragment.app.Fragment
import com.chelz.feature.main.presentation.MainFragment
import com.chelz.libraries.navigation.FragmentDestination

class MainFragmentDestination : FragmentDestination {

	override fun createInstance(): Fragment = MainFragment()

}