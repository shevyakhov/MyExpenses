package com.chelz.libraries.notification

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val NotificationModule = module {
	single { LocalNotificationManager(androidContext()) }
	single { AlarmReceiver() }
}