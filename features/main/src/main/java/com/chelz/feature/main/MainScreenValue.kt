package com.chelz.feature.main

sealed class MainScreenValue {

	object Home : MainScreenValue()
	object Statistics : MainScreenValue()
	object Planning : MainScreenValue()
	object Profile : MainScreenValue()
}