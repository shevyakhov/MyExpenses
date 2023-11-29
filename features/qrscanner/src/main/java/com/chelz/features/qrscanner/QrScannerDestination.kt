package com.chelz.features.qrscanner

import androidx.fragment.app.Fragment
import com.chelz.features.qrscanner.presentation.scanner.QrScannerFragment
import com.chelz.libraries.navigation.FragmentDestination

object QrScannerDestination : FragmentDestination {

	override fun createInstance(): Fragment = QrScannerFragment()
}