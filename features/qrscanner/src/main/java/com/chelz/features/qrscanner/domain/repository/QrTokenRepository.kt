package com.chelz.features.qrscanner.domain.repository

interface QrTokenRepository {

	suspend fun getToken(): String
}