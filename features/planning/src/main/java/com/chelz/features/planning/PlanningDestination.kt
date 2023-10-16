package com.chelz.features.planning

import androidx.fragment.app.Fragment
import com.chelz.features.planning.presentation.PlanningFragment
import com.chelz.libraries.navigation.FragmentDestination

object PlanningDestination : FragmentDestination {

	override fun createInstance(): Fragment = PlanningFragment()
}