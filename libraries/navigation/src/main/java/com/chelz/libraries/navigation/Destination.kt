package com.chelz.libraries.navigation

import android.content.Intent
import androidx.fragment.app.Fragment

sealed interface Destination

interface FragmentDestination : Destination {

	fun createInstance(): Fragment
}