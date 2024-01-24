package com.chelz.myexpenses.di

import com.chelz.features.accountadd.presentation.navigation.AddAccountRouter
import com.chelz.features.accountedit.presentation.navigation.EditAccountRouter
import com.chelz.features.categoryadd.presentation.navigation.AddCategoryRouter
import com.chelz.features.home.navigation.HomeRouter
import com.chelz.features.main.presentation.navigation.MainFragmentRouter
import com.chelz.features.onboarding.presentation.navigation.OnBoardingRouter
import com.chelz.features.planning.presentation.navigation.PlanningRouter
import com.chelz.features.profile.presentation.navigation.ProfileRouter
import com.chelz.features.qrscanner.presentation.result.navigation.QrResultRouter
import com.chelz.features.qrscanner.presentation.scanner.navigation.QrScannerRouter
import com.chelz.features.registration.presentation.navigation.RegistrationRouter
import com.chelz.features.splash.presentation.navigation.SplashRouter
import com.chelz.features.statistics.presentation.navigation.StatisticsRouter
import com.chelz.login.presentation.navigation.LoginRouter
import org.koin.dsl.module

val RouterModule = module {
	factory<SplashRouter> { SplashRouterImpl(get()) }
	factory<OnBoardingRouter> { OnBoardingRouterImpl(get()) }
	factory<RegistrationRouter> { RegistrationRouterImpl(get()) }
	factory<LoginRouter> { LoginRouterImpl(get()) }
	factory<HomeRouter> { HomeRouterImpl(get()) }
	factory<MainFragmentRouter> { MainFragmentRouterImpl(get()) }
	factory<StatisticsRouter> { StatisticsRouterImpl(/*get()*/) }
	factory<PlanningRouter> { PlanningRouterImpl(/*get()*/) }
	factory<ProfileRouter> { ProfileRouterImpl(get()) }
	factory<AddAccountRouter> { AddAccountRouterImpl(get()) }
	factory<EditAccountRouter> { EditAccountRouterImpl(get()) }
	factory<AddCategoryRouter> { AddCategoryRouterImpl(get()) }
	factory<QrScannerRouter> { QrScannerRouterImpl(get()) }
	factory<QrResultRouter> { QrResultRouterImpl(get()) }
}