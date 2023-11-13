package com.chelz.features.statistics.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chelz.features.statistics.databinding.FragmentStatisticsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticsFragment : Fragment() {

	private var _binding: FragmentStatisticsBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<StatisticsViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentStatisticsBinding.inflate(inflater, container, false)
		return binding.root
	}

}