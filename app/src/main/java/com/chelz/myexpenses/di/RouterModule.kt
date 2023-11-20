package com.chelz.myexpenses.di

import com.chelz.features.accountadd.presentation.navigation.AddAccountRouter
import com.chelz.features.home.navigation.HomeRouter
import com.chelz.features.main.presentation.navigation.MainFragmentRouter
import com.chelz.features.planning.presentation.navigation.PlanningRouter
import com.chelz.features.profile.presentation.navigation.ProfileRouter
import com.chelz.features.splash.presentation.navigation.SplashRouter
import com.chelz.features.statistics.presentation.navigation.StatisticsRouter
import org.koin.dsl.module

val RouterModule = module {
	factory<SplashRouter> { SplashRouterImpl(get()) }
	factory<HomeRouter> { HomeRouterImpl(get()) }
	factory<MainFragmentRouter> { MainFragmentRouterImpl(get()) }
	factory<StatisticsRouter> { StatisticsRouterImpl(/*get()*/) }
	factory<PlanningRouter> { PlanningRouterImpl(/*get()*/) }
	factory<ProfileRouter> { ProfileRouterImpl(/*get()*/) }
	factory<AddAccountRouter> { AddAccountRouterImpl(get()) }
}