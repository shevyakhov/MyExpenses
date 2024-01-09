package com.chelz.features.home.navigation

import com.chelz.shared.accounts.domain.entity.AccountItem

interface HomeRouter {

	fun navigateToAddAccount()
	fun navigateToAddCategory()
	fun navigateToQrScanner()
	fun navigateToEditAccount(accountItem: AccountItem)
}