package com.chelz.libraries.navigation.cicerone

class Cicerone<T : BaseRouter> private constructor(val router: T) {

	fun getNavigatorHolder(): NavigatorHolder = router.commandBuffer

	companion object {

		@JvmStatic
		fun create() = create(Router())

		@JvmStatic
		fun <T : BaseRouter> create(customRouter: T) = Cicerone(customRouter)
	}
}