package com.chelz.shared.accounts.domain.entity

import java.io.Serializable

open class AccountItem(
	open val id: Long,
	open val name: String,
	open val number: String,
	open val color: String,
	open var money: Double,
) : Serializable