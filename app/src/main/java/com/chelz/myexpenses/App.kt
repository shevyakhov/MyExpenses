package com.chelz.myexpenses

import android.app.Application
import com.chelz.features.home.di.HomeModule
import com.chelz.features.main.di.MainFragmentModule
import com.chelz.features.planning.di.PlanningModule
import com.chelz.features.profile.di.ProfileModule
import com.chelz.features.splash.di.SplashModule
import com.chelz.features.statistics.di.StatisticsModule
import com.chelz.myexpenses.di.AppModule
import com.chelz.myexpenses.di.RouterModule
import com.chelz.shared.accounts.domain.di.AccountsDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@App)
			modules(AppModule)
			modules(RouterModule)

			modules(MainFragmentModule)
			modules(SplashModule)
			modules(HomeModule)
			modules(StatisticsModule)
			modules(PlanningModule)
			modules(ProfileModule)

			modules(AccountsDataModule)
		}
	}
}