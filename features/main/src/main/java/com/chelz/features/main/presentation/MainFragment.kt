package com.chelz.features.main.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chelz.features.main.MainScreenValue
import com.chelz.features.main.R
import com.chelz.features.main.databinding.FragmentMainBinding
import com.chelz.features.main.presentation.navigation.MainRouterName
import com.chelz.libraries.navigation.cicerone.NavigatorHolder
import com.chelz.libraries.navigation.cicerone.androidx.AppNavigator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MainFragment : Fragment() {

	private val navigatorHolder: NavigatorHolder by inject(named(MainRouterName.MAIN))
	private val viewModel by viewModel<MainFragmentViewModel>()
	private var _binding: FragmentMainBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMainBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initCicerone()
	}

	private fun initCicerone() {
		val navigator = AppNavigator(requireActivity(), R.id.host_main, childFragmentManager)
		navigatorHolder.setNavigator(navigator)

		viewModel.mainScreenValueFlow.onEach {
			binding.bottomNavigation.selectedItemId = when (it) {
				MainScreenValue.Home       -> R.id.home
				MainScreenValue.Statistics -> R.id.stats
				MainScreenValue.Planning   -> R.id.planning
				MainScreenValue.Profile    -> R.id.profile
			}
		}.launchIn(lifecycleScope)


		binding.bottomNavigation.setOnItemSelectedListener {
			if (binding.bottomNavigation.selectedItemId != it.itemId)
				when (it.itemId) {
					R.id.home     -> viewModel.navigateToFoodList()
					R.id.stats    -> viewModel.navigateToRecipes()
					R.id.planning -> viewModel.navigateToToBuyList()
					R.id.profile  -> viewModel.navigateToProfile()
				}
			true
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
		navigatorHolder.removeNavigator()
	}
}