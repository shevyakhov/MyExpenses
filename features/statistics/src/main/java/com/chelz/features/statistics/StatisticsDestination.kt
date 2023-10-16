package com.chelz.features.statistics

import androidx.fragment.app.Fragment
import com.chelz.features.statistics.presentation.StatisticsFragment
import com.chelz.libraries.navigation.FragmentDestination

object StatisticsDestination : FragmentDestination {

	override fun createInstance(): Fragment = StatisticsFragment()
}