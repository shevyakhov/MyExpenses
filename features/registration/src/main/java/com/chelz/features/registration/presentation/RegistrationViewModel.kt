package com.chelz.features.registration.presentation

import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.registration.presentation.navigation.RegistrationRouter
import com.chelz.shared.accounts.domain.entity.User
import com.chelz.shared.accounts.domain.usecase.user.AddUserUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
	private val router: RegistrationRouter,
	private val insertUserUseCase: AddUserUseCase,
) : ViewModel() {

	private val auth by lazy { Firebase.auth }
	private val _regError = MutableSharedFlow<String?>()
	val regError: SharedFlow<String?> = _regError

	private val _nameError = MutableStateFlow<String?>(null)
	val nameError: StateFlow<String?> = _nameError

	private val _emailError = MutableStateFlow<String?>(null)
	val emailError: StateFlow<String?> = _emailError

	private val _passwordError = MutableStateFlow<String?>(null)
	val passwordError: StateFlow<String?> = _passwordError

	fun navigateToLogin() {
		router.navigateToLogin()
	}

	fun register(name: String, email: String, password: String) = viewModelScope.launch {
		val isValid = validateName(name)
			&& validateEmail(email)
			&& validatePassword(password)
		if (!isValid) return@launch

		val user = User(0, name, email)
		auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
			viewModelScope.launch {
				insertUserUseCase(user)
			}
			router.navigateToLogin()

		}.addOnFailureListener {
			viewModelScope.launch {
				_regError.emit(it.localizedMessage)
			}
		}
	}

	private fun validatePassword(password: String): Boolean =
		if (password.isEmpty()) {
			_passwordError.value = "Password cannot be empty"
			false
		} else {
			_passwordError.value = null
			true
		}

	private fun validateEmail(email: String): Boolean =
		if (!EMAIL_ADDRESS.matcher(email).matches()) {
			_emailError.value = "Invalid email address"
			false
		} else {
			_emailError.value = null
			true
		}

	private fun validateName(name: String): Boolean =
		if (name.length < 3) {
			_nameError.value = "Name should be at least 3 characters"
			false
		} else {
			_nameError.value = null
			true
		}
}