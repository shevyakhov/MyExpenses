package com.chelz.features.accountadd

import androidx.fragment.app.Fragment
import com.chelz.features.accountadd.presentation.AddAccountFragment
import com.chelz.libraries.navigation.FragmentDestination

object AddAccountDestination : FragmentDestination {

	override fun createInstance(): Fragment = AddAccountFragment()
}