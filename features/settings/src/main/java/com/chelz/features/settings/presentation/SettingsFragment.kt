package com.chelz.features.settings.presentation

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chelz.features.settings.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

	private var _binding: FragmentSettingsBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<SettingsViewModel>()
	private var notificationEnabled = false
	private val requestPermissionLauncher =
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
			notificationEnabled = isGranted
		}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentSettingsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope
		val time = viewModel.getPreferredTime()?.split(":")
		binding.timePickerNotification.hour = time?.first()?.toInt() ?: 14
		binding.timePickerNotification.minute = time?.component2()?.toInt() ?: 0

		binding.switchGraphs.isChecked = viewModel.getSwitchGraphsFlow()
		binding.switchHistory.isChecked = viewModel.getSwitchHistoryFlow()
		binding.switchDailyExpenseReminder.isChecked = viewModel.getSwitchDailyExpenseReminderFlow()
		binding.switchWeeklyStatsReminder.isChecked = viewModel.getSwitchWeeklyStatsReminderFlow()
		binding.switchTodayStats.isChecked = viewModel.getSwitchTodayStatsFlow()

		binding.switchGraphs.setOnCheckedChangeListener { _, b ->
			viewModel.switchGraphsFlow.value = b
		}
		binding.switchHistory.setOnCheckedChangeListener { _, b ->
			viewModel.switchHistoryFlow.value = b
		}
		binding.switchDailyExpenseReminder.setOnCheckedChangeListener { _, b ->
			requestPermissionLauncher.launch(Manifest.permission.SET_ALARM)
			if (notificationEnabled) {
				viewModel.switchDailyExpenseReminderFlow.value = b
			}
		}
		binding.switchWeeklyStatsReminder.setOnCheckedChangeListener { _, b ->
			requestPermissionLauncher.launch(Manifest.permission.SET_ALARM)
			if (notificationEnabled) {
				viewModel.switchWeeklyStatsReminderFlow.value = b
			}
		}
		binding.switchTodayStats.setOnCheckedChangeListener { _, b ->
			viewModel.switchTodayStatsFlow.value = b
		}

		binding.timePickerNotification.setIs24HourView(true)
		binding.timePickerNotification.setOnTimeChangedListener { timePicker, hour, min ->
			viewModel.preferredTimeFlow.value = "${hour}:${min}"
		}

		binding.buttonSave.setOnClickListener {
			viewModel.switchGraphsFlow.value = binding.switchGraphs.isChecked
			viewModel.switchHistoryFlow.value = binding.switchHistory.isChecked
			viewModel.switchDailyExpenseReminderFlow.value = binding.switchDailyExpenseReminder.isChecked
			viewModel.switchWeeklyStatsReminderFlow.value = binding.switchWeeklyStatsReminder.isChecked
			viewModel.switchTodayStatsFlow.value = binding.switchTodayStats.isChecked
			viewModel.savePreferredTime()
			viewModel.saveSettings()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}