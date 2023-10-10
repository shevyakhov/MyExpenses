package com.chelz.myexpenses.di

import com.chelz.libraries.navigation.GlobalRouter
import com.chelz.libraries.navigation.MainRouter
import com.chelz.myexpenses.di.GlobalNavigatorName.GLOBAL
import com.chelz.myexpenses.di.MainNavigatorName.MAIN
import com.chelz.myexpenses.presentation.MainViewModel
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GlobalNavigatorName {

	const val GLOBAL = "GLOBAL"
}

object MainNavigatorName {

	const val MAIN = "MAIN"
}

fun buildCicerone(): Cicerone<Router> = Cicerone.create()

val AppModule = module {
	single(named(GLOBAL)) { buildCicerone() }
	single(named(GLOBAL)) { get<Cicerone<Router>>(named(GLOBAL)).router }
	single(named(GLOBAL)) { get<Cicerone<Router>>(named(GLOBAL)).getNavigatorHolder() }
	single<GlobalRouter> { GlobalRouterImpl(get(named(GLOBAL))) }

	single(named(MAIN)) { buildCicerone() }
	single(named(MAIN)) { get<Cicerone<Router>>(named(MAIN)).router }
	single(named(MAIN)) { get<Cicerone<Router>>(named(MAIN)).getNavigatorHolder() }
	single<MainRouter> { MainRouterImpl(get(named(MAIN))) }

	viewModel { MainViewModel(router = get()) }
}