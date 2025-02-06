package com.chelz.libraries.navigation.cicerone

import android.os.Handler
import android.os.Looper

internal class CommandBuffer : NavigatorHolder {

	private var navigator: Navigator? = null
	private val pendingCommands = mutableListOf<Array<out Command>>()
	private val mainHandler = Handler(Looper.getMainLooper())

	override fun setNavigator(navigator: Navigator) {
		this.navigator = navigator
		pendingCommands.forEach { navigator.applyCommands(it) }
		pendingCommands.clear()
	}

	override fun removeNavigator() {
		navigator = null
	}

	fun executeCommands(commands: Array<out Command>) {
		mainHandler.post {
			navigator?.applyCommands(commands) ?: pendingCommands.add(commands)
		}
	}
}