package com.chelz.myexpenses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chelz.libraries.navigation.cicerone.NavigatorHolder
import com.chelz.libraries.navigation.cicerone.androidx.AppNavigator
import com.chelz.myexpenses.di.GlobalNavigatorName.GLOBAL
import com.chelz.myexpenses.presentation.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {

	private val navigatorHolder: NavigatorHolder by inject(named(GLOBAL))
	private val navigator = AppNavigator(this, R.id.host_global)
	private val auth by lazy { Firebase.auth }
	private val viewModel by viewModel<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		navigatorHolder.setNavigator(navigator)
		val firstStart = getSharedPreferences("USER", MODE_PRIVATE).getBoolean("FIRST_START", true)
		val currentUser = auth.currentUser
		if (currentUser != null) {
			viewModel.openMainRoot(firstStart)
		} else
			viewModel.openMainRoot(true)
	}

	override fun onDestroy() {
		super.onDestroy()
		navigatorHolder.removeNavigator()

	}
}