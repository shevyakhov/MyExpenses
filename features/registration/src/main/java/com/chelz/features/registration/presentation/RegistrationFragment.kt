package com.chelz.features.registration.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chelz.features.registration.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RegistrationFragment : Fragment() {

	private val viewModel: RegistrationViewModel by viewModel {
		parametersOf(
			requireContext().getSharedPreferences("USER", Context.MODE_PRIVATE).getBoolean("FIRST_START", true)
		)
	}
	private var _binding: FragmentRegistrationBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentRegistrationBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope
		viewModel.regError.onEach {
			Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
		}.launchIn(scope)
		viewModel.nameError.onEach {
			binding.textInputLayoutName.error = it
		}.launchIn(scope)
		viewModel.emailError.onEach {
			binding.textInputLayoutEmail.error = it
		}.launchIn(scope)
		viewModel.passwordError.onEach {
			binding.textInputLayoutPassword.error = it
		}.launchIn(scope)

		binding.buttonRegister.setOnClickListener {
			val name = binding.editTextName.text.toString()
			val email = binding.editTextEmail.text.toString()
			val password = binding.editTextPassword.text.toString()
			viewModel.register(name, email, password)
		}

		binding.buttonLogin.setOnClickListener {
			viewModel.navigateToLogin()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}