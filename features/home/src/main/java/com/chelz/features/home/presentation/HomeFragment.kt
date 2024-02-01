package com.chelz.features.home.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chelz.features.home.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<HomeViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.init()

		val isGraphVisible = getSwitchGraphsFlow()
		val isHistoryVisible = getSwitchHistoryFlow()
		val isStatsVisible = getSwitchTodayStatsFlow()
		binding.mainLayout.bind(viewModel, viewLifecycleOwner, isGraphVisible, isHistoryVisible, isStatsVisible)
		binding.slidingLayout.bind(viewModel, viewLifecycleOwner)
	}

	private fun getSwitchGraphsFlow(): Boolean {
		return requireActivity()
			.applicationContext
			.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(GRAPHS, true)
	}

	fun getSwitchHistoryFlow(): Boolean {
		return requireActivity()
			.applicationContext
			.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(HISTORY, true)
	}

	private fun getSwitchTodayStatsFlow(): Boolean {
		return requireActivity()
			.applicationContext
			.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(TODAY_STATS, true)
	}

	companion object {

		const val SETTINGS_PREFERENCE = "SETTINGS_PREFERENCE"
		const val GRAPHS = "switchGraphsFlow"
		const val HISTORY = "switchHistoryFlow"
		const val TODAY_STATS = "switchTodayStatsFlow"
	}

}