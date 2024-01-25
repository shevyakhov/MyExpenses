package com.chelz.features.settings

import androidx.fragment.app.Fragment
import com.chelz.features.settings.presentation.SettingsFragment
import com.chelz.libraries.navigation.FragmentDestination

object SettingsDestination : FragmentDestination {

	override fun createInstance(): Fragment {
		return SettingsFragment()
	}
}