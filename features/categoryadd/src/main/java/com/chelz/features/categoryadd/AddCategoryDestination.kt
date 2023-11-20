package com.chelz.features.categoryadd

import androidx.fragment.app.Fragment
import com.chelz.features.categoryadd.presentation.AddCategoryFragment
import com.chelz.libraries.navigation.FragmentDestination

object AddCategoryDestination : FragmentDestination {

	override fun createInstance(): Fragment {
		return AddCategoryFragment()
	}
}