package com.chelz.features.qrscanner.domain

interface ScanningResultListener {

	fun onScanned(result: String)
}
