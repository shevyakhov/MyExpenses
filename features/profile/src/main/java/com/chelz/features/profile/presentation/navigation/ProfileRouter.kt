package com.chelz.features.profile.presentation.navigation

import com.chelz.shared.accounts.domain.entity.AccountItem

interface ProfileRouter {

	fun navigateToEditAccount(accountItem: AccountItem)
	fun navigateToLogin()
}