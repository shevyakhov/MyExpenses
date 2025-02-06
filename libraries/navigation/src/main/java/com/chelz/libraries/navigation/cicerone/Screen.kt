package com.chelz.libraries.navigation.cicerone

interface Screen {

	val screenKey: String get() = this::class.java.name
}