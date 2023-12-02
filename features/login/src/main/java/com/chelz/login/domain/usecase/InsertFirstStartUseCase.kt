package com.chelz.login.domain.usecase

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertFirstStartUseCase(private val context: Context) {

	suspend operator fun invoke() = withContext(Dispatchers.IO) {
		val editor = context.getSharedPreferences("USER", AppCompatActivity.MODE_PRIVATE).edit()
		editor.putBoolean("FIRST_START", false).apply()
	}
}
