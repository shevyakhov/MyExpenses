package com.chelz.features.registration

import androidx.fragment.app.Fragment
import com.chelz.features.registration.presentation.RegistrationFragment
import com.chelz.libraries.navigation.FragmentDestination

object RegistrationDestination : FragmentDestination {

	override fun createInstance(): Fragment =
		RegistrationFragment()
}