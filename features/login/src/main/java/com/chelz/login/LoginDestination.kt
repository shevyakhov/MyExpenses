package com.chelz.login

import androidx.fragment.app.Fragment
import com.chelz.libraries.navigation.FragmentDestination
import com.chelz.login.presentation.LoginFragment

object LoginDestination : FragmentDestination {

	override fun createInstance(): Fragment =
		LoginFragment()
}