package com.chelz.features.qrscanner.data.repository

import com.chelz.features.qrscanner.data.datasource.QrTokenDataSource
import com.chelz.features.qrscanner.domain.repository.QrTokenRepository

class QrTokenRepositoryImpl(private val dataSource: QrTokenDataSource) : QrTokenRepository {

	override suspend fun getToken(): String {
		return dataSource.getToken()
	}
}