package com.chelz.features.statistics.domain

sealed class TabState {
	object Account : TabState()
	object Category : TabState()

	companion object {

		fun toTabState(position: Int): TabState =
			when (position) {
				0    -> Account
				1    -> Category
				else -> error("no Such State")
			}
	}
}
