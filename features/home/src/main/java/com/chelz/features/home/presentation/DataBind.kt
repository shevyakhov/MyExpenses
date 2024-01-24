package com.chelz.features.home.presentation

import android.graphics.Color
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.chelz.features.home.R
import com.chelz.features.home.databinding.BottomLayoutBinding
import com.chelz.features.home.databinding.MainLayoutBinding
import com.chelz.features.home.presentation.recycler.accounts.AccountViewPagerAdapter
import com.chelz.features.home.presentation.recycler.accounts.HorizontalMarginItemDecoration
import com.chelz.features.home.presentation.recycler.categories.CategoryAdapter
import com.chelz.features.home.presentation.recycler.categories.CategoryClickListener

import com.chelz.features.home.presentation.recycler.operations.OperationAdapter
import com.chelz.libraries.theme.getThemeColor
import com.chelz.shared.accounts.domain.entity.CategoryItem
import com.chelz.shared.accounts.domain.entity.toCategory
import com.chelz.shared.accounts.domain.entity.toCategoryItem
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.StrictMath.abs

internal fun MainLayoutBinding.bind(viewModel: HomeViewModel, viewLifecycleOwner: LifecycleOwner) {
	val scope = viewLifecycleOwner.lifecycleScope
	val accountAdapter = AccountViewPagerAdapter {
		viewModel.navigateToEditAccount(it)
	}
	val operationAdapter = OperationAdapter()
	screenName.setOnClickListener {
		viewModel.navigateToAddAccount()
	}
	rvOperations.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
	rvOperations.adapter = operationAdapter


	viewModel.sharedOperationFlow.onEach {
		operationAdapter.setNewData(it)
		rvOperations.smoothSnapToPosition(0)
	}.launchIn(scope)

	viewModel.todaySpend.onEach {
		statsQuantity.text = it.toString()
	}.launchIn(scope)

	viewModel.weekSpend.onEach { week ->
		val labels: List<String> = listOf("", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
		val entries: MutableList<Entry> = ArrayList()
		for (day in 1..7) {
			val list = week[day] ?: emptyList()
			val totalSpendForDay = list.sumOf { abs(it.quantity) }
			entries.add(Entry(day.toFloat(), totalSpendForDay.toFloat()))
		}

		val lineDataSet = LineDataSet(entries, "Weekly Spend")
		lineDataSet.color = Color.BLUE
		lineDataSet.valueTextColor = Color.BLACK
		lineDataSet.valueTextSize = 14f
		lineDataSet.lineWidth = 2f
		lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
		lineDataSet.setCircleColors(Color.BLUE)
		lineDataSet.circleRadius = 3f
		val lineData = LineData(lineDataSet)
		chartWeekly.data = lineData

		val xAxis = chartWeekly.xAxis
		xAxis.valueFormatter = IndexAxisValueFormatter(labels)
		xAxis.textSize = 14f

		val description = Description()
		description.text = "Weekly Spend Progress"
		chartWeekly.setExtraOffsets(10f, 10f, 10f, 20f)
		chartWeekly.description = description
		chartWeekly.invalidate()
	}.launchIn(scope)

	val itemDecoration = HorizontalMarginItemDecoration(root.context, R.dimen.viewpager_current_item_horizontal_margin)
	accountViewPager.adapter = accountAdapter
	accountViewPager.apply {
		setPadding(48, 0, 48, 0)
		offscreenPageLimit = 1
		val nextItemVisiblePx = root.resources.getDimension(R.dimen.viewpager_next_item_visible)
		val currentItemHorizontalMarginPx = root.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
		val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
		setPageTransformer { page: View, position: Float ->
			page.translationX = -pageTranslationX * position
			page.scaleY = 1 - (0.25f * abs(position))
		}
		addItemDecoration(itemDecoration)
	}
	accountViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
		override fun onPageSelected(position: Int) {
			super.onPageSelected(position)
			viewModel.currentAccount.value = accountAdapter.getItemAt(position)
		}
	})

	viewModel.fullAccountsFlow.onEach {
		accountAdapter.initList(it)
	}.launchIn(scope)
}

internal fun BottomLayoutBinding.bind(viewModel: HomeViewModel, viewLifecycleOwner: LifecycleOwner) {
	val scope = viewLifecycleOwner.lifecycleScope
	val adapter = CategoryAdapter(object : CategoryClickListener {
		override fun onClick(categoryItem: CategoryItem?) {
			val category = categoryItem?.toCategory()
			viewModel.onItemClick(category)
		}

	})
	rvCategories.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
	rvCategories.adapter = adapter

	viewModel.categoriesFlow.onEach {
		adapter.setData(viewModel.categoriesFlow.value.filter { category -> !category.isEarning }.toCategoryItem())
	}.launchIn(scope)

	viewModel.stringFlow.onEach {
		sum.text = it
	}.launchIn(scope)

	viewModel.isEarningFlow.onEach {
		if (it) {
			typeText.text = root.context.getString(R.string.earning)
			enter.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorPrimary))
			typeText.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorPrimary))
			reverse.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorPrimary))
			erase.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorPrimary))
			delete.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorPrimary))
			adapter.setData(viewModel.categoriesFlow.value.filter { category -> category.isEarning }.toCategoryItem())
		} else {
			typeText.text = root.context.getString(R.string.expense)
			enter.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorError))
			typeText.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorError))
			erase.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorError))
			delete.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorError))
			reverse.setBackgroundColor(getThemeColor(root.context, com.google.android.material.R.attr.colorError))
			adapter.setData(viewModel.categoriesFlow.value.filter { category -> !category.isEarning }.toCategoryItem())
		}

	}.launchIn(scope)


	zero.setOnClickListener { viewModel.addZero() }
	one.setOnClickListener { viewModel.addOne() }
	two.setOnClickListener { viewModel.addTwo() }
	three.setOnClickListener { viewModel.addThree() }
	four.setOnClickListener { viewModel.addFour() }
	five.setOnClickListener { viewModel.addFive() }
	six.setOnClickListener { viewModel.addSix() }
	seven.setOnClickListener { viewModel.addSeven() }
	eight.setOnClickListener { viewModel.addEight() }
	nine.setOnClickListener { viewModel.addNine() }
	dot.setOnClickListener { viewModel.addDot() }

	qr.setOnClickListener { viewModel.onQrClick() }
	enter.setOnClickListener { viewModel.onEnter() }
	reverse.setOnClickListener { viewModel.onReverseClick() }
	delete.setOnClickListener { viewModel.delete() }
	erase.setOnClickListener { viewModel.erase() }

	addCategoryButton.setOnClickListener { viewModel.navigateToAddCategory() }
}

internal fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
	val smoothScroller = object : LinearSmoothScroller(this.context) {
		override fun getVerticalSnapPreference(): Int = snapMode
		override fun getHorizontalSnapPreference(): Int = snapMode
	}
	smoothScroller.targetPosition = position
	layoutManager?.startSmoothScroll(smoothScroller)
}