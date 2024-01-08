package com.chelz.features.statistics.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.util.Pair
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.chelz.features.statistics.R
import com.chelz.features.statistics.databinding.FragmentStatisticsBinding
import com.chelz.features.statistics.domain.DatePeriod
import com.chelz.features.statistics.domain.TabState
import com.chelz.features.statistics.presentation.adapter.HorizontalMarginItemDecoration
import com.chelz.features.statistics.presentation.adapter.SwipeItemAdapter
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.CategoryItem
import com.chelz.shared.accounts.domain.entity.OperationItem
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.datepicker.MaterialDatePicker.Builder
import com.google.android.material.datepicker.MaterialDatePicker.thisMonthInUtcMilliseconds
import com.google.android.material.datepicker.MaterialDatePicker.todayInUtcMilliseconds
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.joda.time.LocalDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

class StatisticsFragment : Fragment() {

	private var _binding: FragmentStatisticsBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<StatisticsViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentStatisticsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope
		viewModel.init()

		bindData(scope)
	}

	private fun bindData(scope: LifecycleCoroutineScope) {
		bindFilters(scope)
		bindGraphs(scope)
	}

	private fun bindFilters(scope: LifecycleCoroutineScope) {
		val adapter = SwipeItemAdapter()
		binding.swipeViewPagerItems.adapter = adapter
		bindTabAdapter(adapter)
		bindFilterTabs(adapter, scope)
		binding.earningCheckBox.addOnCheckedStateChangedListener { _, _ ->
			if (viewModel.currentTab.value is TabState.Category) {
				adapter.updateCategory(viewModel.categoriesFlow.value.filter { it.isEarning == binding.earningCheckBox.isChecked })
			}

			invalidateLineChart(viewModel.chartOperationFlow.value)
			invalidateBarChart(viewModel.chartOperationFlow.value)
			invalidatePieChart(viewModel.chartOperationFlow.value)
			invalidateSemiPieChart(viewModel.chartOperationFlow.value, viewModel.currentTab.value is TabState.Account)
		}
	}

	private fun bindGraphs(scope: LifecycleCoroutineScope) {
		binding.radioGroupTimePeriod.setOnCheckedChangeListener { _, i ->
			val checkedRadioButton = view?.findViewById<RadioButton>(i)
			val checkedIndex = binding.radioGroupTimePeriod.indexOfChild(checkedRadioButton)
			if (checkedIndex != DatePeriod.DAY_CUSTOM.ordinal) {
				viewModel.currentDatePeriodFlow.value = DatePeriod.values()[checkedIndex]
			} else {
				val dateRangePicker =
					Builder.dateRangePicker()
						.setTitleText("Select dates")
						.setSelection(
							Pair(
								thisMonthInUtcMilliseconds(),
								todayInUtcMilliseconds()
							)
						)
						.build()

				dateRangePicker.show(childFragmentManager, "Date")
				dateRangePicker.addOnPositiveButtonClickListener {
					viewModel.startDateFlow.value = it.first
					viewModel.endDateFlow.value = it.second
					viewModel.currentDatePeriodFlow.value = DatePeriod.values()[checkedIndex]
				}
			}
		}

		viewModel.currentDatePeriodFlow.onEach {
			viewModel.drawChart()
		}.launchIn(scope)

		viewModel.chartOperationFlow.onEach {
			invalidateLineChart(it)
			invalidateBarChart(it)
			invalidatePieChart(it)
			invalidateSemiPieChart(it, viewModel.currentTab.value is TabState.Account)
		}.launchIn(scope)
		binding.tabLayoutGraphs.addOnTabSelectedListener(object : OnTabSelectedListener {
			override fun onTabSelected(tab: TabLayout.Tab?) {
				when (tab?.position) {
					0 -> bindLineChart()
					1 -> bindBarChart()
					2 -> bindPieChart()
				}
			}

			override fun onTabUnselected(tab: TabLayout.Tab?) {}
			override fun onTabReselected(tab: TabLayout.Tab?) {}
		})
	}

	private fun getDatesInRange(startDate: Date, endDate: Date): List<Date> {
		val dates = mutableListOf<Date>()
		val calendar = Calendar.getInstance()
		calendar.time = startDate
		while (calendar.time.before(endDate) || calendar.time == endDate) {
			dates.add(calendar.time)
			calendar.add(Calendar.DATE, 1)
		}
		return dates
	}

	private fun Date.toLocalDate(): LocalDate {
		val calendar = Calendar.getInstance()
		calendar.time = this
		return LocalDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
	}

	private fun invalidateLineChart(it: List<OperationItem>) {
		val map = viewModel.sumValuesByDay(it, getCheckedEarningValue())

		val earliestDate = map.keys.minOrNull()?.toDate() ?: Date()
		val latestDate = map.keys.maxOrNull()?.toDate() ?: Date()
		val dateRange = getDatesInRange(earliestDate, latestDate)

		val entries = mutableListOf<Entry>()
		val labels = mutableListOf<String>()

		dateRange.forEachIndexed { _, date ->
			val localDate = date.toLocalDate()
			val value = map[localDate]

			value?.let {
				entries.add(Entry(entries.size.toFloat(), it.toFloat()))
				labels.add(localDate.toString("dd.MM"))
			}
		}
		val dataSet = LineDataSet(entries, "Ваши траты")
		dataSet.color = Color.BLUE
		dataSet.valueTextColor = Color.BLACK
		dataSet.valueTextSize = 14f
		dataSet.lineWidth = 2f
		dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
		dataSet.setCircleColors(Color.BLUE)
		dataSet.circleRadius = 3f
		val data = LineData(dataSet)

		binding.chartLine.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

		binding.chartLine.data = data
		binding.chartLine.invalidate()
	}

	private fun invalidateBarChart(it: List<OperationItem>) {
		val map = viewModel.sumValuesByDay(it, getCheckedEarningValue())

		val earliestDate = map.keys.minOrNull()?.toDate() ?: Date()
		val latestDate = map.keys.maxOrNull()?.toDate() ?: Date()
		val dateRange = getDatesInRange(earliestDate, latestDate)

		val entries = mutableListOf<BarEntry>()
		val labels = mutableListOf<String>()

		dateRange.forEachIndexed { index, date ->
			val localDate = date.toLocalDate()
			val value = map[localDate]

			value?.let {
				entries.add(BarEntry(index.toFloat(), it.toFloat()))
				labels.add(localDate.toString("dd.MM"))
			}
		}
		val barDataSet = BarDataSet(entries, "Ваши траты")
		barDataSet.color = Color.BLUE
		barDataSet.color = Color.BLUE
		barDataSet.valueTextColor = Color.BLACK
		barDataSet.valueTextSize = 14f
		barDataSet.barBorderWidth = 2f
		barDataSet.setColors(Color.BLUE)

		val data = BarData(barDataSet)
		binding.chartBar.data = data

		val xAxis = binding.chartBar.xAxis
		xAxis.position = XAxis.XAxisPosition.BOTTOM
		xAxis.valueFormatter = IndexAxisValueFormatter(labels)
		binding.chartBar.invalidate()
	}

	private fun invalidatePieChart(it: List<OperationItem>) {
		val map = viewModel.sumValuesByDay(it, getCheckedEarningValue())

		val entries = mutableListOf<PieEntry>()
		map.forEach { (localDate, value) ->
			entries.add(PieEntry(value.toFloat(), localDate.toString("dd.MM")))
		}

		val pieDataSet = PieDataSet(entries, "Ваши траты")
		pieDataSet.valueTextSize = 20f
		pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

		val data = PieData(pieDataSet)

		val pieChart = binding.chartPie
		pieChart.data = data
		pieChart.invalidate()

		pieChart.setDrawEntryLabels(false)
	}

	private fun invalidateSemiPieChart(operationItems: List<OperationItem>, isAccount: Boolean) {
		val dataEntries = ArrayList<PieEntry>()

		val groupedItems = if (isAccount) {
			operationItems.filter { it.quantity * getCheckedEarningValue() < 0 }.groupBy { it.category?.name ?: "Unknown Category" }
		} else {
			operationItems.filter { it.quantity * getCheckedEarningValue() < 0 }.groupBy { it.account }
		}

		groupedItems.forEach { (key, items) ->
			val quantity = items.sumOf { abs(it.quantity) }
			dataEntries.add(PieEntry(quantity.toFloat(), key))
		}

		val dataSet = PieDataSet(dataEntries, if (isAccount) "Categories" else "Accounts")
		dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
		dataSet.valueTextColor = Color.BLACK
		dataSet.valueTextSize = 12f
		dataSet.valueFormatter = PercentFormatter(binding.semiChartPie)

		val pieData = PieData(dataSet)
		binding.semiChartPie.data = pieData

		binding.semiChartPie.description.isEnabled = false
		binding.semiChartPie.isDrawHoleEnabled = true
		binding.semiChartPie.setHoleColor(Color.WHITE)
		binding.semiChartPie.setDrawEntryLabels(false)

		binding.semiChartPie.invalidate()
	}

	private fun getCheckedEarningValue(): Int {
		return if (binding.earningCheckBox.isChecked) -1 else 1
	}

	private fun bindTabAdapter(adapter: SwipeItemAdapter) {
		val itemDecoration = HorizontalMarginItemDecoration(binding.root.context, R.dimen.viewpager_current_item_horizontal_margin)

		binding.swipeViewPagerItems.apply {
			setPadding(48, 0, 48, 0)
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
		binding.swipeViewPagerItems.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				val item = adapter.getItemAt(position)
				if (item is AccountItem) {
					viewModel.currentAccount.value = item
				} else if (item is CategoryItem) {
					viewModel.currentCategory.value = item
				}
			}
		})
	}

	private fun bindFilterTabs(adapter: SwipeItemAdapter, scope: LifecycleCoroutineScope) {
		binding.tabLayoutFilters.addOnTabSelectedListener(object : OnTabSelectedListener {
			override fun onTabSelected(tab: TabLayout.Tab?) {
				if (tab?.position == 0) {
					viewModel.currentTab.value = TabState.Account
				} else
					viewModel.currentTab.value = TabState.Category
			}

			override fun onTabUnselected(tab: TabLayout.Tab?) {}
			override fun onTabReselected(tab: TabLayout.Tab?) {}
		})
		viewModel.currentTab.onEach { state ->
			when (state) {
				TabState.Account  -> {
					binding.reverseText.text = getString(R.string.categories)
					adapter.updateAccount(viewModel.fullAccountsFlow.value)
					val id = viewModel.fullAccountsFlow.value.indexOf(viewModel.currentAccount.value ?: return@onEach)
					binding.swipeViewPagerItems.setCurrentItem(id, false)
					binding.reverseText.text = getString(R.string.categories)
				}

				TabState.Category -> {
					binding.reverseText.text = getString(R.string.your_accounts)
					adapter.updateCategory(viewModel.categoriesFlow.value.filter { it.isEarning == binding.earningCheckBox.isChecked })
					val id = viewModel.categoriesFlow.value.indexOf(viewModel.currentCategory.value ?: return@onEach)
					binding.swipeViewPagerItems.setCurrentItem(id, false)
				}
			}
		}.launchIn(scope)

		viewModel.categoriesFlow.onEach { categoryItems ->
			if (viewModel.currentTab.value == TabState.Category) {
				adapter.updateCategory(categoryItems.filter { it.isEarning == binding.earningCheckBox.isChecked })
				val id = viewModel.categoriesFlow.value.indexOf(viewModel.currentCategory.value)
				binding.swipeViewPagerItems.setCurrentItem(id, true)
			}
		}.launchIn(scope)
		viewModel.fullAccountsFlow.onEach {
			if (viewModel.currentTab.value == TabState.Account) {
				adapter.updateAccount(it)
				binding.swipeViewPagerItems.setCurrentItem(0, true)
			}
		}.launchIn(scope)
	}

	private fun bindPieChart() {
		binding.chartPie.isInvisible = false
		binding.chartBar.isInvisible = true
		binding.chartLine.isInvisible = true
	}

	private fun bindBarChart() {
		binding.chartBar.isInvisible = false
		binding.chartPie.isInvisible = true
		binding.chartLine.isInvisible = true
	}

	private fun bindLineChart() {
		binding.chartLine.isInvisible = false
		binding.chartBar.isInvisible = true
		binding.chartPie.isInvisible = true
	}
}
