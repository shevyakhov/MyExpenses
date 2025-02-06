package com.chelz.libraries.navigation.cicerone

abstract class BaseRouter {

	internal val commandBuffer = CommandBuffer()
	private val resultWire = ResultWire()

	fun setResultListener(
		key: String,
		listener: ResultListener
	): ResultListenerHandler {
		return resultWire.setResultListener(key, listener)
	}

	fun sendResult(key: String, data: Any) {
		resultWire.sendResult(key, data)
	}

	protected fun executeCommands(vararg commands: Command) {
		commandBuffer.executeCommands(commands)
	}
}