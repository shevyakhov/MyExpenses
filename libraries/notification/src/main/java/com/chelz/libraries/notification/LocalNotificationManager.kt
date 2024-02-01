package com.chelz.libraries.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.chelz.libraries.notification.AlarmReceiver.Companion.NOTIFICATION_ACTION
import com.chelz.libraries.notification.AlarmReceiver.Companion.NOTIFICATION_ID_DAILY
import com.chelz.libraries.notification.AlarmReceiver.Companion.NOTIFICATION_ID_WEEKLY
import org.joda.time.DateTime
import java.util.Calendar

class LocalNotificationManager(private val context: Context) {

	fun scheduleDailyNotification(time: String) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, AlarmReceiver::class.java)
		intent.putExtra(NOTIFICATION_ACTION, NOTIFICATION_ID_DAILY)
		val title = dailyTitles.random()
		val message = dailyMessages.random()

		intent.putExtra("title", title)
		intent.putExtra("message", message)

		val pendingIntent = PendingIntent.getBroadcast(
			context,
			0,
			intent,
			PendingIntent.FLAG_UPDATE_CURRENT
		)
		val timer = time.split(":")

		val hour = timer.first()
		val min = timer.last()

		val calendar: Calendar = Calendar.getInstance().apply {
			timeInMillis = System.currentTimeMillis()
			set(Calendar.HOUR_OF_DAY, hour.toInt())
			set(Calendar.MINUTE, min.toInt())
		}
		alarmManager.setInexactRepeating(
			AlarmManager.RTC_WAKEUP,
			calendar.timeInMillis,
			AlarmManager.INTERVAL_DAY,
			pendingIntent
		)
	}

	fun scheduleWeeklyNotification(dayOfWeek: Int, time: String) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, AlarmReceiver::class.java)
		intent.putExtra(NOTIFICATION_ACTION, NOTIFICATION_ID_WEEKLY)
		val title = weeklyTitles.random()
		val message = weeklyMessages.random()

		intent.putExtra("message", message)
		intent.putExtra("title", title)

		val pendingIntent = PendingIntent.getBroadcast(
			context,
			1,
			intent,
			PendingIntent.FLAG_UPDATE_CURRENT
		)

		val triggerTimeMillis = getMillisForDayAndTime(dayOfWeek, time)
		alarmManager.setInexactRepeating(
			AlarmManager.RTC_WAKEUP,
			triggerTimeMillis,
			AlarmManager.INTERVAL_DAY * 7,
			pendingIntent
		)
	}

	private fun getMillisForTime(time: String): Long {
		val now = DateTime.now()
		val targetTime = DateTime.parse(now.toString("yyyy-MM-dd") + "T$time")
		return if (targetTime.isBeforeNow) {
			targetTime.plusDays(1).millis
		} else {
			targetTime.millis
		}
	}

	private fun getMillisForDayAndTime(dayOfWeek: Int, time: String): Long {
		val now = DateTime.now()
		val daysUntilTarget = (dayOfWeek - now.dayOfWeek) % 7
		val targetDate = now.plusDays(daysUntilTarget)
		val targetTime = DateTime.parse(targetDate.toString("yyyy-MM-dd") + "T$time")
		return if (targetTime.isBeforeNow) {
			targetTime.plusWeeks(1).millis
		} else {
			targetTime.millis
		}
	}

	fun cancelNotification(notificationId: Int) {
		val notificationManager =
			context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.cancel(notificationId)
	}

	// Cancel all notifications
	private fun cancelAllNotifications() {
		val notificationManager =
			context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.cancelAll()
	}

	private companion object {

		val dailyTitles = listOf(
			"Финансовая дисциплина — ключ к успеху!",
			"Экономим с умом!",
			"Заботьтесь о каждом рубле",
			"Контролируйте финансы каждый день",
			"Финансовое здоровье начинается сегодня"
		)

		val dailyMessages = listOf(
			"Не забудьте учесть все расходы сегодня. Каждая запятая в бюджете — шаг к вашим финансовым целям!",
			"Ваш ежедневный напоминатель: проверьте свои расходы и задайте себе вопрос, можно ли сэкономить. Ваши деньги, ваши правила!",
			"Добрый день! Помните о финансах. Запишите свои расходы и почувствуйте контроль над своим бюджетом.",
			"Сегодняшний день — отличный момент для отслеживания расходов. Бюджетирование помогает воплощать мечты в жизнь!",
			"Важно знать, куда уходят ваши деньги. Не забудьте записать свои расходы сегодня!"
		)

		val weeklyTitles = listOf(
			"Взгляд назад, взлет вперёд!",
			"Анализ успеха каждую неделю",
			"Еженедельная оценка: куда мы идем?",
			"Неделя в цифрах: успехи и возможности",
			"Взгляд на достижения недели"
		)

		val weeklyMessages = listOf(
			"Смотрите на свой недельный прогресс и готовьтесь к новым достижениям. Вперёд, к успеху!",
			"Отразитесь на своих достижениях за неделю и подумайте, как сделать следующую ещё лучше. Перспектива — ключ к развитию!",
			"Время оценить свои усилия на этой неделе. Сделайте шаг назад, чтобы сделать два шага вперёд!",
			"Прошедшая неделя была удивительной! Посмотрите на статистику и задайте себе вопрос: что можно улучшить?",
			"Посмотрите на свои успехи на прошлой неделе. Это отличный способ подготовиться к новым вызовам!"
		)
	}
}