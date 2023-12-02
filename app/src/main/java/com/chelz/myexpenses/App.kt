package com.chelz.myexpenses

import android.app.Application
import com.chelz.features.accountadd.di.AddAccountModule
import com.chelz.features.categoryadd.di.AddCategoryFragmentModule
import com.chelz.features.home.di.HomeModule
import com.chelz.features.main.di.MainFragmentModule
import com.chelz.features.onboarding.di.OnBoardingModule
import com.chelz.features.planning.di.PlanningModule
import com.chelz.features.profile.di.ProfileModule
import com.chelz.features.qrscanner.di.QrModule
import com.chelz.features.registration.di.RegistrationModule
import com.chelz.features.splash.di.SplashModule
import com.chelz.features.statistics.di.StatisticsModule
import com.chelz.login.di.LoginModule
import com.chelz.myexpenses.BuildConfig.QR_KEY
import com.chelz.myexpenses.BuildConfig.QR_URL
import com.chelz.myexpenses.di.AppModule
import com.chelz.myexpenses.di.RouterModule
import com.chelz.network.BACKEND_QR
import com.chelz.network.BACKEND_QR_KEY
import com.chelz.network.NetworkModule
import com.chelz.shared.accounts.domain.di.AccountsDataModule
import com.chelz.shared.accounts.domain.di.AccountsDomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@App)
			properties(
				mapOf(
					BACKEND_QR to QR_URL,
					BACKEND_QR_KEY to QR_KEY,
				)
			)
			modules(AppModule)
			modules(RouterModule)
			modules(NetworkModule)

			modules(MainFragmentModule)
			modules(SplashModule)
			modules(OnBoardingModule)
			modules(RegistrationModule)
			modules(LoginModule)

			modules(HomeModule)
			modules(StatisticsModule)
			modules(PlanningModule)
			modules(ProfileModule)
			modules(AddAccountModule)
			modules(AddCategoryFragmentModule)

			modules(AccountsDataModule)
			modules(AccountsDomainModule)
			modules(QrModule)
		}
	}
}