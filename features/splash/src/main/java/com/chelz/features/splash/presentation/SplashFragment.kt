package com.chelz.features.splash.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.chelz.feature.splash.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SplashFragment : Fragment() {
	companion object {

		private const val ANIMATION_START_DELAY = 500L
		private const val FIRST_START_FLAG = "FIRST_START_FLAG"

		fun newInstance(firstStartFlag: Boolean): SplashFragment =
			SplashFragment().apply {
				arguments = bundleOf(FIRST_START_FLAG to firstStartFlag)
			}
	}

	private val viewModel: SplashViewModel by viewModel {
		parametersOf(
			false
		)
	}
	private var _binding: FragmentSplashBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentSplashBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.init()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}