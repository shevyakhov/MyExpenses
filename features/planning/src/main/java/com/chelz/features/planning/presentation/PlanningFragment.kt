package com.chelz.features.planning.presentation

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.chelz.features.planning.R
import com.chelz.features.planning.databinding.FragmentPlanningBinding
import com.chelz.features.planning.presentation.adapter.AccountPlanningViewPagerAdapter
import com.chelz.features.planning.presentation.adapter.CategoryPlanningAdapter
import com.chelz.features.planning.presentation.adapter.HorizontalMarginItemDecoration
import com.chelz.libraries.theme.getThemeColor
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.Category
import com.chelz.shared.accounts.domain.entity.MonthGoal
import com.chelz.shared.accounts.domain.entity.toCategory
import com.chelz.shared.accounts.domain.entity.toCategoryItem
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs
import com.google.android.material.R as MaterialR

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
		val scope = viewLifecycleOwner.lifecycleScope

		viewModel.init()
		binding.deleteGoal.setOnClickListener {
			deleteGoal()
		}
		binding.buttonAddGoal.setOnClickListener {
			showLimitGoalDialog {
				viewModel.insertGoal(it)
			}
		}
		binding.calendarReload.setOnClickListener {
			bindCalendarReload()
		}
		binding.calendar.setOnClickListener {
			callDateDialog()
		}


		bindAdapters(scope)
	}

	private fun bindAdapters(scope: LifecycleCoroutineScope) {
		val accountAdapter = AccountPlanningViewPagerAdapter()
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
				renderGraph(viewModel.currentAccount.value, viewModel.currentCategory.value)
			}
		})
		viewModel.fullAccountsFlow.onEach {
			accountAdapter.initList(it)
		}.launchIn(scope)
		val categoryAdapter = CategoryPlanningAdapter()

		binding.rvCategories.adapter = categoryAdapter
		binding.rvCategories.apply {
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
		binding.earningCheckBox.addOnCheckedStateChangedListener { _, _ ->
			categoryAdapter.setData(viewModel.categoriesFlow.value.filter { it.isEarning == binding.earningCheckBox.isChecked }.toCategoryItem())
		}
		binding.rvCategories.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				viewModel.currentCategory.value = categoryAdapter.getItemAt(position).toCategory()
				renderGraph(viewModel.currentAccount.value, viewModel.currentCategory.value)
			}
		})

		viewModel.categoriesFlow.onEach {
			val items = viewModel.categoriesFlow.value.toCategoryItem().filter { it.isEarning == binding.earningCheckBox.isChecked }
			categoryAdapter.setData(items)
		}.launchIn(scope)

		viewModel.currentCalendar.onEach {
			val account = viewModel.currentAccount.value
			val category = viewModel.currentCategory.value
			renderGraph(account, category)
		}.launchIn(scope)

		viewModel.allMonthGoals.onEach {
			val account = viewModel.currentAccount.value
			val category = viewModel.currentCategory.value
			renderGraph(account, category)
		}.launchIn(scope)
	}

	private fun bindCalendarReload() {
		viewModel.currentCalendar.value = DateTime.now().toString("MM-YYYY")
		binding.calendarReload.isVisible = false
		binding.textCalendarReload.isVisible = false
		binding.calendar.isVisible = true
		renderGraph(viewModel.currentAccount.value, viewModel.currentCategory.value)
	}

	private fun deleteGoal() {
		val goal = viewModel.currentGoal.value ?: return
		val builder = MaterialAlertDialogBuilder(requireContext())
		builder.setTitle(getString(R.string.are_you_sure))
		builder.setMessage(
			buildSpannedString {
				append(getString(R.string.delete_goal))
				bold {
					append("\n${goal.category} - ${goal.yearMonth}")
				}
			}
		)

		builder.setPositiveButton(getString(R.string.delete)) { dialog, _ ->
			viewModel.deleteGoal(goal)
		}
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
			dialog.dismiss()
		}
		builder.create().show()
	}

	fun showLimitGoalDialog(listener: (Pair<String, String>) -> Unit) {
		val builder = MaterialAlertDialogBuilder(requireContext())
		builder.setTitle("Добавьте цель на месяц ${viewModel.currentCalendar.value}")
		builder.setMessage(
			"Установите лимит " +
				"для категории ${viewModel.currentCategory.value?.name} " +
				"на счёте ${viewModel.currentAccount.value?.name}"
		)

		val layout = LinearLayout(context).apply {
			orientation = LinearLayout.VERTICAL
			setPadding(10)
		}

		val goalNameTextView = EditText(context).apply {
			hint = "Название цели"
			setText("Месячный лимит")
		}

		val limitEditText = EditText(context).apply {
			hint = "Ограничение"
			inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
		}
		layout.addView(goalNameTextView)
		layout.addView(limitEditText)
		builder.setView(layout)

		builder.setPositiveButton(getString(R.string.add)) { dialog, _ ->
			val name = goalNameTextView.text.toString()
			val limit = limitEditText.text.toString()
			listener.invoke(name to limit)
			dialog.dismiss()
		}
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
			dialog.dismiss()
		}
		builder.create().show()
	}

	private fun renderGraph(
		account: AccountItem?,
		category: Category?,
	) {
		val goals = viewModel.searchMonthGoalsByDate(viewModel.currentCalendar.value)
		val list = goals.filter { goal ->
			goal.account in (account?.name ?: "") &&
				goal.category == category?.name
		}
		if (list.isEmpty()) {
			renderEmptyList()
		} else {
			val goal = list.first()
			renderGoal(goal)
		}
	}

	private fun renderEmptyList() {
		viewModel.currentGoal.value = null
		binding.lineChart.isVisible = false
		binding.progressHorizontal.isVisible = false
		binding.deleteGoal.isVisible = false
		binding.buttonAddGoal.isVisible = true
	}

	private fun renderGoal(goal: MonthGoal) {
		viewModel.currentGoal.value = goal
		binding.lineChart.isVisible = true
		binding.progressHorizontal.isVisible = true
		binding.deleteGoal.isVisible = true
		binding.buttonAddGoal.isVisible = false
		binding.lineChart.clear()

		val operations = viewModel.sharedOperationFlow.value
		val filteredOperations = operations.filter {
			convertDateFormat(it.date) == goal.yearMonth && goal.account in it.account && goal.category == it.category?.name
		}.sortedBy { it.date }

		val dailySums = mutableMapOf<Int, Double>()
		val today = DateTime.now().dayOfMonth().get()
		val totalDaysInMonth = if (goal.yearMonth == DateTime.now().toString("MM-yyyy")) {
			today
		} else {
			DateTime.parse(goal.yearMonth, DateTimeFormat.forPattern("MM-yyyy")).dayOfMonth().maximumValue
		}

		for (dayOfMonth in 1..totalDaysInMonth) {
			dailySums[dayOfMonth] = 0.0
		}
		for (operation in filteredOperations) {
			val dayOfMonth = operation.date.substringBefore("-").toInt()
			dailySums[dayOfMonth] = abs(operation.quantity)
		}
		for (dayOfMonth in 2..totalDaysInMonth) {
			dailySums[dayOfMonth] = (dailySums[dayOfMonth] ?: 0.0) + (dailySums[dayOfMonth - 1] ?: 0.0)
		}

		val entries = mutableListOf<Entry>()
		dailySums.forEach { (day, sum) ->
			entries.add(Entry(day.toFloat(), sum.toFloat()))
		}
		binding.progressHorizontal.setProgressSmoothly(filteredOperations.sumOf { abs(it.quantity) } / goal.monthlyLimit * 100)

		val dataSet = LineDataSet(entries, "Daily Sum")
		dataSet.color = primaryColor()
		dataSet.setCircleColor(primaryColor())
		dataSet.valueTextColor = Color.BLACK
		dataSet.valueTextSize = 14f
		dataSet.lineWidth = 2f
		dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
		dataSet.setCircleColors(primaryColor())
		dataSet.circleRadius = 3f
		dataSet.apply {
			setDrawValues(true)
			valueFormatter = object : ValueFormatter() {
				private var previousValue: Float? = null

				override fun getPointLabel(entry: Entry?): String {
					val currentValue = entry?.y ?: return ""
					val label = if (previousValue == null || previousValue != currentValue) {
						currentValue.toString()
					} else {
						""
					}
					previousValue = currentValue
					return label
				}
			}
		}

		val limitLineValue = goal.monthlyLimit.toFloat()
		val limitLine = LimitLine(limitLineValue, "Цель")
		limitLine.lineWidth = 2f
		limitLine.enableDashedLine(10f, 10f, 0f)

		val currentProgress = filteredOperations.sumOf { abs(it.quantity) }

		binding.progressHorizontal.progress = (currentProgress / goal.monthlyLimit * 100).toInt()

		val yAxisMin = 0f
		val yAxisMax = if ((dailySums.values.maxOrNull() ?: 0.0) > limitLineValue) {
			((dailySums.values.maxOrNull() ?: 0.0) * 1.05).toFloat()
		} else {
			limitLineValue * 1.05f
		}


		binding.lineChart.apply {
			description.isEnabled = true
			xAxis.setDrawGridLines(true)
			axisRight.isEnabled = true
			axisLeft.axisMinimum = yAxisMin
			axisLeft.axisMaximum = yAxisMax
			axisLeft.removeAllLimitLines()
			axisLeft.addLimitLine(limitLine)

			data = LineData(dataSet)
			invalidate()
		}
	}

	private fun primaryColor() = getThemeColor(requireContext(), MaterialR.attr.colorTertiary)

	private fun callDateDialog() {
		val builder = MonthYearPickerDialog.Builder(
			context = requireContext(),
			themeResId = R.style.MyMonthYearPickerDialog,
			onDateSetListener = { year, month ->
				binding.calendar.isVisible = false
				binding.calendarReload.isVisible = true
				binding.textCalendarReload.isVisible = true
				val formattedDate = formatMonthYear(month + 1, year)
				binding.textCalendarReload.text = "${month + 1}-$year"
				viewModel.currentCalendar.value = formattedDate
			},
		).build()
		builder.show()
	}

	private fun formatMonthYear(month: Int, year: Int): String {
		val formatter = DateTimeFormat.forPattern("MM-yyyy")
		return formatter.print(DateTime(year, month, 1, 0, 0))
	}

	private fun convertDateFormat(dateString: String): String {
		val inputFormat = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss.SSS")

		val dateTime = inputFormat.parseDateTime(dateString)

		val outputFormat = DateTimeFormat.forPattern("MM-yyyy")

		return outputFormat.print(dateTime)
	}
}