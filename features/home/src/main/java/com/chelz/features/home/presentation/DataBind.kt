package com.chelz.features.home.presentation

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
import com.chelz.features.home.presentation.recycler.accounts.toAccount
import com.chelz.features.home.presentation.recycler.accounts.toSliderItem
import com.chelz.features.home.presentation.recycler.categories.CategoryAdapter
import com.chelz.features.home.presentation.recycler.categories.CategoryClickListener
import com.chelz.features.home.presentation.recycler.categories.CategoryItem
import com.chelz.features.home.presentation.recycler.categories.toCategory
import com.chelz.features.home.presentation.recycler.categories.toCategoryItem
import com.chelz.features.home.presentation.recycler.operations.OperationAdapter
import com.chelz.libraries.theme.getThemeColor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.StrictMath.abs

internal fun MainLayoutBinding.bind(viewModel: HomeViewModel, viewLifecycleOwner: LifecycleOwner) {
	val scope = viewLifecycleOwner.lifecycleScope
	val accountAdapter = AccountViewPagerAdapter()
	val operationAdapter = OperationAdapter()
	screenName.setOnClickListener {
		viewModel.navigateToAddAccount()
	}
	rvOperations.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
	rvOperations.adapter = operationAdapter


	viewModel.operationItemFlow.onEach {
		operationAdapter.setNewData(it)
		rvOperations.smoothSnapToPosition(0)
	}.launchIn(scope)

	viewModel.todaySpend.onEach {
		statsQuantity.text = it.toString()
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
			viewModel.currentAccount.value = accountAdapter.getItemAt(position).toAccount()
		}
	})

	viewModel.accountsFlow.onEach {
		accountAdapter.initList(it.toSliderItem())
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