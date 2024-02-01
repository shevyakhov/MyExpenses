package com.chelz.myexpenses.di

import com.chelz.features.accountedit.EditAccountDestination
import com.chelz.features.profile.presentation.navigation.ProfileRouter
import com.chelz.features.settings.SettingsDestination
import com.chelz.libraries.navigation.GlobalRouter
import com.chelz.login.LoginDestination
import com.chelz.shared.accounts.domain.entity.AccountItem

class ProfileRouterImpl(private val router: GlobalRouter) : ProfileRouter {

	override fun navigateToEditAccount(accountItem: AccountItem) {
		router.open(EditAccountDestination(accountItem))
	}

	override fun navigateToLogin() {
		router.replace(LoginDestination)
	}

	override fun navigateToSettings() {
		router.open(SettingsDestination)
	}
}