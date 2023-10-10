package com.chelz.myexpenses.di

import com.chelz.features.splash.presentation.navigation.SplashRouter
import com.github.terrakok.cicerone.Router
import org.koin.dsl.module

val RouterModule = module {
	factory<SplashRouter> { SplashRouterImpl(get()) }
}