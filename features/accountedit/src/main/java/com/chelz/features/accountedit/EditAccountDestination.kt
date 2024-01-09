package com.chelz.features.accountedit

import androidx.fragment.app.Fragment
import com.chelz.features.accountedit.presentation.EditAccountFragment
import com.chelz.libraries.navigation.FragmentDestination
import com.chelz.shared.accounts.domain.entity.AccountItem

class EditAccountDestination(private val accountItem: AccountItem) : FragmentDestination {

	override fun createInstance(): Fragment =
		EditAccountFragment.newInstance(accountItem)
}
