package com.chelz.libraries.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		val message = intent.getStringExtra("message")
		val title = intent.getStringExtra("title")
		when (intent.getIntExtra(NOTIFICATION_ACTION, 0)) {
			NOTIFICATION_ID_DAILY  -> showDailyNotification(context, title ?: "", message ?: "")
			NOTIFICATION_ID_WEEKLY -> showWeeklyNotification(context, title ?: "", message ?: "")
			else                   -> Unit
		}
	}

	private fun showDailyNotification(context: Context, title: String, message: String) {
		createNotificationChannel(context, "daily_channel")

		val notificationBuilder = NotificationCompat.Builder(context, "daily_channel")
			.setSmallIcon(R.drawable.ic_back)
			.setContentTitle(title)
			.setContentText(message)
			.setStyle(NotificationCompat.BigTextStyle().bigText(message))
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)

		with(NotificationManagerCompat.from(context)) {
			if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SET_ALARM) == PackageManager.PERMISSION_GRANTED) {
				notify(NOTIFICATION_ID_DAILY, notificationBuilder.build())
			}
		}
	}

	private fun showWeeklyNotification(context: Context, title: String, message: String) {
		createNotificationChannel(context, "weekly_channel")

		val notificationBuilder = NotificationCompat.Builder(context, "weekly_channel")
			.setSmallIcon(R.drawable.ic_back)
			.setContentTitle(title)
			.setContentText(message)
			.setStyle(NotificationCompat.BigTextStyle().bigText(message))
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)

		with(NotificationManagerCompat.from(context)) {
			if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SET_ALARM) == PackageManager.PERMISSION_GRANTED) {
				notify(NOTIFICATION_ID_WEEKLY, notificationBuilder.build())
			}
		}
	}

	private fun createNotificationChannel(context: Context, channelId: String) {
		val name = when (channelId) {
			"daily_channel"  -> "Ежедневные напоминания"
			"weekly_channel" -> "Еженедельные напоминания"
			else             -> "Default Channel"
		}
		val descriptionText = "Channel for showing reminders"
		val importance = NotificationManager.IMPORTANCE_DEFAULT
		val channel = NotificationChannel(channelId, name, importance).apply {
			description = descriptionText
		}
		val notificationManager: NotificationManager =
			context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(channel)
	}

	companion object {

		const val NOTIFICATION_ID_DAILY = 1
		const val NOTIFICATION_ACTION = "NOTIFICATION_ACTION"
		const val NOTIFICATION_ID_WEEKLY = 2
	}
}
