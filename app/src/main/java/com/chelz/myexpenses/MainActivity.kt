package com.chelz.myexpenses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chelz.myexpenses.di.GlobalNavigatorName.GLOBAL
import com.chelz.myexpenses.presentation.MainViewModel
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {
	private val navigatorHolder: NavigatorHolder by inject(named(GLOBAL))
	private val navigator = AppNavigator(this, R.id.host_global)

	private val viewModel by viewModel<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		navigatorHolder.setNavigator(navigator)
		viewModel.openMainRoot()
	}
	override fun onDestroy() {
		super.onDestroy()
		navigatorHolder.removeNavigator()

	}
}