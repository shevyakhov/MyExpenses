package com.chelz.features.home.domain

import com.chelz.shared.accounts.domain.entity.Category

class SharedCategory(
	override var name: String,
	override var isEarning: Boolean = false,
	override var color: String = "#FF0000",
) : Category(-1L, name, isEarning, color)