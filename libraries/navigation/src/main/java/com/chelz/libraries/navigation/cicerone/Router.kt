package com.chelz.libraries.navigation.cicerone

open class Router : BaseRouter() {

	fun navigateTo(screen: Screen) {
		executeCommands(Forward(screen))
	}

	fun newRootScreen(screen: Screen) {
		executeCommands(BackTo(null), Replace(screen))
	}

	fun replaceScreen(screen: Screen) {
		executeCommands(Replace(screen))
	}

	fun backTo(screen: Screen?) {
		executeCommands(BackTo(screen))
	}

	fun newChain(vararg screens: Screen) {
		val commands = screens.map { Forward(it) }
		executeCommands(*commands.toTypedArray())
	}

	fun newRootChain(vararg screens: Screen) {
		val commands = screens.mapIndexed { index, screen ->
			if (index == 0)
				Replace(screen)
			else
				Forward(screen)
		}
		executeCommands(BackTo(null), *commands.toTypedArray())
	}

	fun finishChain() {
		executeCommands(BackTo(null), Back())
	}

	fun exit() {
		executeCommands(Back())
	}
}