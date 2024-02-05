package com.chelz.features.planning.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.chelz.features.planning.R
import com.chelz.features.planning.databinding.FragmentPlanningBinding
import com.chelz.features.planning.presentation.adapter.AccountPlanningViewPagerAdapter
import com.chelz.features.planning.presentation.adapter.CategoryPlanningAdapter
import com.chelz.features.planning.presentation.adapter.HorizontalMarginItemDecoration
import com.chelz.shared.accounts.domain.entity.toCategory
import com.chelz.shared.accounts.domain.entity.toCategoryItem
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanningFragment : Fragment() {

	private var _binding: FragmentPlanningBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<PlanningViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentPlanningBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.init()
		binding.calendarReload.setOnClickListener {
			viewModel.getAllMonthGoals()
			binding.calendarReload.isVisible = false
			binding.textCalendarReload.isVisible = false
			binding.calendar.isVisible = true
		}
		binding.calendar.setOnClickListener {
			callDateDialog()
		}
		val scope = viewLifecycleOwner.lifecycleScope
		val accountAdapter = AccountPlanningViewPagerAdapter {}
		val itemDecoration = HorizontalMarginItemDecoration(binding.root.context, R.dimen.viewpager_current_item_horizontal_margin)
		binding.accountViewPager.adapter = accountAdapter
		binding.accountViewPager.apply {
			offscreenPageLimit = 1
			val nextItemVisiblePx = binding.root.resources.getDimension(R.dimen.viewpager_next_item_visible)
			val currentItemHorizontalMarginPx = binding.root.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
			val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
			setPageTransformer { page: View, position: Float ->
				page.translationX = -pageTranslationX * position
				page.scaleY = 1 - (0.25f * StrictMath.abs(position))
			}
			addItemDecoration(itemDecoration)
		}
		binding.accountViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				viewModel.currentAccount.value = accountAdapter.getItemAt(position)
			}
		})
		viewModel.fullAccountsFlow.onEach {
			accountAdapter.initList(it)
		}.launchIn(scope)

		val categoryAdapter = CategoryPlanningAdapter()
		binding.rvCategories.adapter = categoryAdapter
		binding.rvCategories.apply {
			setIntervalRatio(1f)
			setAlpha(true)
			setInfinite(true)
		}
		binding.earningCheckBox.addOnCheckedStateChangedListener { _, _ ->
			categoryAdapter.setData(viewModel.categoriesFlow.value.filter { it.isEarning == binding.earningCheckBox.isChecked }.toCategoryItem())
		}
		binding.rvCategories.setItemSelectListener(object : CarouselLayoutManager.OnSelected {
			override fun onItemSelected(position: Int) {
				viewModel.currentCategory.value = categoryAdapter.getItemAt(position).toCategory()
			}
		})
		viewModel.categoriesFlow.onEach {
			val items = viewModel.categoriesFlow.value.toCategoryItem().filter { it.isEarning == binding.earningCheckBox.isChecked }
			categoryAdapter.setData(items)
		}.launchIn(scope)
	}

	private fun callDateDialog() {
		val builder = MonthYearPickerDialog.Builder(
			context = requireContext(),
			themeResId = R.style.MyMonthYearPickerDialog,
			onDateSetListener = { year, month ->
				binding.calendar.isVisible = false
				binding.calendarReload.isVisible = true
				binding.textCalendarReload.isVisible = true
				binding.textCalendarReload.text = "${month + 1}.$year"
				viewModel.searchMonthGoalsByDate("$month.$year")
			},
		).build()
		builder.show()
	}
}