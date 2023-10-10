package com.chelz.myexpenses

import android.app.Application
import com.chelz.features.splash.di.SplashModule
import com.chelz.myexpenses.di.AppModule
import com.chelz.myexpenses.di.RouterModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@App)
			modules(AppModule)
			modules(RouterModule)

			modules(SplashModule)
		}
	}
}