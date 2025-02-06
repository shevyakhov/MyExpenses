package com.chelz.libraries.navigation.cicerone

fun interface ResultListener {

	fun onResult(data: Any)
}

fun interface ResultListenerHandler {

	fun dispose()
}

internal class ResultWire {

	private val listeners = mutableMapOf<String, ResultListener>()

	fun setResultListener(key: String, listener: ResultListener): ResultListenerHandler {
		listeners[key] = listener
		return ResultListenerHandler {
			listeners.remove(key)
		}
	}

	fun sendResult(key: String, data: Any) {
		listeners.remove(key)?.onResult(data)
	}
}