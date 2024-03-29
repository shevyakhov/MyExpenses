package com.chelz.features.settings.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chelz.features.settings.presentation.navigation.SettingsRouter
import com.chelz.libraries.notification.LocalNotificationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
	private val router: SettingsRouter,
	private val application: Application,
	private val notificationManager: LocalNotificationManager,
) : AndroidViewModel(application) {

	companion object {

		const val TIME_PREFERENCE = "TIME_PREFERENCE"
		const val TIME_PREFERENCE_KEY = "TIME_PREFERENCE_KEY"
		const val DAILY_NOTIFY = 0
		const val WEEKLY_NOTIFY = 1

		const val SETTINGS_PREFERENCE = "SETTINGS_PREFERENCE"
		const val GRAPHS = "switchGraphsFlow"
		const val HISTORY = "switchHistoryFlow"
		const val DAILY = "switchDailyExpenseReminderFlow"
		const val WEEKLY = "switchWeeklyStatsReminderFlow"
		const val TODAY_STATS = "switchTodayStatsFlow"
	}

	val switchGraphsFlow = MutableStateFlow(true)
	val switchHistoryFlow = MutableStateFlow(true)
	val switchDailyExpenseReminderFlow = MutableStateFlow(true)
	val switchWeeklyStatsReminderFlow = MutableStateFlow(true)
	val switchTodayStatsFlow = MutableStateFlow(true)

	val preferredTimeFlow = MutableStateFlow<String?>(null)

	fun saveSettings() {
		saveSwitchGraphsFlow()
		saveSwitchHistoryFlow()
		saveSwitchDailyExpenseReminderFlow()
		saveSwitchWeeklyStatsReminderFlow()
		saveSwitchTodayStatsFlow()
		router.navigateBack()
	}

	fun savePreferredTime() = viewModelScope.launch {
		val sharedPreferences = application.applicationContext.getSharedPreferences(TIME_PREFERENCE, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		preferredTimeFlow.value?.let {
			editor.putString(TIME_PREFERENCE_KEY, it)
		}
		editor.apply()
	}

	fun getPreferredTime(): String? {
		return application
			.applicationContext
			.getSharedPreferences(TIME_PREFERENCE, Context.MODE_PRIVATE)
			.getString(TIME_PREFERENCE_KEY, "14:00")
	}

	fun saveSwitchGraphsFlow() = viewModelScope.launch {
		val sharedPreferences = application.applicationContext.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		switchGraphsFlow.value.let {
			editor.putBoolean(GRAPHS, it)
		}
		editor.apply()
	}

	fun getSwitchGraphsFlow(): Boolean {
		return application
			.applicationContext
			.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(GRAPHS, true)
	}

	fun saveSwitchHistoryFlow() = viewModelScope.launch {
		val sharedPreferences = application.applicationContext.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		switchHistoryFlow.value.let {
			editor.putBoolean(HISTORY, it)
		}
		editor.apply()
	}

	fun getSwitchHistoryFlow(): Boolean {
		return application
			.applicationContext
			.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(HISTORY, true)
	}

	fun saveSwitchDailyExpenseReminderFlow() = viewModelScope.launch {
		val sharedPreferences = application.applicationContext.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		switchDailyExpenseReminderFlow.value.let {
			if (it) {
				notificationManager.scheduleDailyNotification(
					preferredTimeFlow.value ?: "14:00"
				)
			} else {
				notificationManager.cancelNotification(DAILY_NOTIFY)
			}
			editor.putBoolean(DAILY, it)
		}
		editor.apply()
	}

	fun getSwitchDailyExpenseReminderFlow(): Boolean {
		return application
			.applicationContext
			.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(DAILY, false)
	}

	fun saveSwitchWeeklyStatsReminderFlow() = viewModelScope.launch {
		val sharedPreferences = application.applicationContext.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		switchWeeklyStatsReminderFlow.value.let {
			if (it) {
				notificationManager.scheduleWeeklyNotification(
					7,
					preferredTimeFlow.value ?: "14:00"
				)
			} else {
				notificationManager.cancelNotification(WEEKLY_NOTIFY)
			}
			editor.putBoolean(WEEKLY, it)
		}
		editor.apply()
	}

	fun getSwitchWeeklyStatsReminderFlow(): Boolean {
		return application
			.applicationContext
			.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(WEEKLY, false)
	}

	fun saveSwitchTodayStatsFlow() = viewModelScope.launch {
		val sharedPreferences = application.applicationContext.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()

		switchTodayStatsFlow.value.let {
			editor.putBoolean(TODAY_STATS, it)
		}
		editor.apply()
	}

	fun getSwitchTodayStatsFlow(): Boolean {
		return application
			.applicationContext
			.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE)
			.getBoolean(TODAY_STATS, true)
	}

}