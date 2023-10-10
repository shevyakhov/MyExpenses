package com.chelz.libraries.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AppNavigator constructor(
	activity: FragmentActivity,
	containerId: Int,
) : AppNavigator(
	activity, containerId
) {

	companion object {

		const val BACK_VIEW_KEY = "BACK_VIEW_KEY"
	}

	override fun commitNewFragmentScreen(screen: FragmentScreen, addToBackStack: Boolean) {
		val nextFragment = screen.createFragment(fragmentFactory)
		val currentFragment = fragmentManager.findFragmentById(containerId)
		nextFragment.putCurrentFragmentScreenName(currentFragment)

		val fragmentTransaction = fragmentManager.beginTransaction()
		fragmentTransaction.setReorderingAllowed(true)
		setupFragmentTransaction(screen, fragmentTransaction, currentFragment, nextFragment)
		fragmentTransaction.setNextFragment(screen, nextFragment, addToBackStack, containerId)
		fragmentTransaction.commit()
	}

	private fun Fragment.putCurrentFragmentScreenName(currentFragment: Fragment?) {
		val lastScreenNameHolder = currentFragment as? ScreenNameHolder
		lastScreenNameHolder?.screenName?.let {
			arguments = (arguments ?: Bundle()).apply { putString(BACK_VIEW_KEY, it) }
		}
	}

	private fun FragmentTransaction.setNextFragment(nextFragmentScreen: FragmentScreen, nextFragment: Fragment, addToBackStack: Boolean, containerId: Int) {
		if (nextFragmentScreen.clearContainer) {
			replace(containerId, nextFragment, nextFragmentScreen.screenKey)
		} else {
			add(containerId, nextFragment, nextFragmentScreen.screenKey)
		}
		if (addToBackStack) {
			addToBackStack(nextFragmentScreen.screenKey)
			localStackCopy.add(nextFragmentScreen.screenKey)
		}
	}

	override fun setupFragmentTransaction(
		screen: FragmentScreen,
		fragmentTransaction: FragmentTransaction,
		currentFragment: Fragment?,
		nextFragment: Fragment,
	) {
		super.setupFragmentTransaction(
			screen,
			fragmentTransaction.setCustomAnimations(
				R.anim.anim_slide_in_right,
				R.anim.anim_slide_out_right,
				R.anim.anim_slide_in_right,
				R.anim.anim_slide_out_right,
			),
			currentFragment,
			nextFragment
		)
	}
}