package com.chelz.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.login.domain.usecase.InsertFirstStartUseCase
import com.chelz.login.presentation.navigation.LoginRouter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
	private val router: LoginRouter,
	private val insertFirstStartUseCase: InsertFirstStartUseCase,
) : ViewModel() {

	private val auth by lazy { Firebase.auth }
	private val _regError = MutableSharedFlow<String?>()
	val regError: SharedFlow<String?> = _regError

	fun navigateToRegistration() {
		router.navigateToRegistration()
	}

	private fun navigateToHome() {
		router.navigateToHomeScreen()
	}

	fun login(email: String, password: String) = viewModelScope.launch {
		auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
			viewModelScope.launch {
				insertFirstStartUseCase.invoke()
			}
			navigateToHome()
		}.addOnFailureListener {
			viewModelScope.launch {
				_regError.emit(it.localizedMessage)
			}
		}
	}
}
