package com.chelz.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chelz.features.login.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

	private val viewModel: LoginViewModel by viewModel()
	private var _binding: FragmentLoginBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentLoginBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope

		viewModel.regError.onEach {
			Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
		}.launchIn(scope)

		binding.buttonLogin.setOnClickListener {
			val email = binding.editTextEmail.text.toString()
			val password = binding.editTextPassword.text.toString()
			viewModel.login(email, password)
		}

		binding.buttonRegistration.setOnClickListener {
			viewModel.navigateToRegistration()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}