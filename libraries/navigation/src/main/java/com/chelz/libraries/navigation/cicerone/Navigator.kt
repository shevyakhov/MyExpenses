package com.chelz.libraries.navigation.cicerone

interface Navigator {

	fun applyCommands(commands: Array<out Command>)
}