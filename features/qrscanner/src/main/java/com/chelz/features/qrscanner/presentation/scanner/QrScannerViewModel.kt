package com.chelz.features.qrscanner.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.qrscanner.data.mapper.toHtmlEntity
import com.chelz.features.qrscanner.data.mapper.toItemEntities
import com.chelz.features.qrscanner.domain.usecase.GetQrResultUseCase
import com.chelz.features.qrscanner.domain.usecase.GetTokenUseCase
import com.chelz.features.qrscanner.presentation.scanner.navigation.QrScannerRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class QrScannerViewModel(
	private val router: QrScannerRouter,
	private val getQrResultUseCase: GetQrResultUseCase,
	private val getTokenUseCase: GetTokenUseCase,
) : ViewModel() {

	val htmlFlow = MutableSharedFlow<String>()
	fun onQrScanned(raw: String) = viewModelScope.launch {
		val token = getTokenUseCase()
		val result = getQrResultUseCase(raw, token)
		val htmlEntity = result.toHtmlEntity()
		val items = result.toItemEntities()

		htmlFlow.emit(htmlEntity.html)
		router.navigateToQrResults(items, htmlEntity)
	}
}