package com.chelz.features.profile.presentation

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chelz.features.profile.R
import com.chelz.features.profile.databinding.FragmentProfileBinding
import com.chelz.features.profile.presentation.adapter.AccountViewPagerAdapter
import com.chelz.features.profile.presentation.adapter.ArticlesAdapter
import com.chelz.features.profile.presentation.adapter.CategoryAdapter
import com.chelz.features.profile.presentation.adapter.CategoryClickListener
import com.chelz.features.profile.presentation.adapter.HorizontalMarginItemDecoration
import com.chelz.shared.accounts.domain.entity.CategoryItem
import com.chelz.shared.accounts.domain.entity.toCategory
import com.chelz.shared.accounts.domain.entity.toCategoryItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

	private var _binding: FragmentProfileBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<ProfileViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentProfileBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope
		viewModel.init()

		val newsAdapter = ArticlesAdapter(emptyList()) // Initial empty list
		binding.recyclerNews.adapter = newsAdapter
		binding.recyclerNews.layoutManager = LinearLayoutManager(
			requireContext(),
			LinearLayoutManager.HORIZONTAL,
			false
		)
		viewModel.newsFlow.onEach {
			binding.newsLabel.isVisible = it.isNotEmpty()
			newsAdapter.submitData(it)
		}.launchIn(scope)

		binding.settingsButton.setOnClickListener {
			viewModel.navigateToSettings()
		}

		binding.exitButton.setOnClickListener {
			val builder = MaterialAlertDialogBuilder(requireContext())
			val message = SpannableStringBuilder()
				.append(getString(R.string.logout_message))

			builder.setMessage(message)
				.setTitle(getString(R.string.logout_title))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.ok)) { _, _ ->
					viewModel.logOut()
				}
				.setNeutralButton(getString(R.string.logout_message_after)) { dialog, _ ->
					viewModel.logOutAndDelete()
					val settings = requireContext().getSharedPreferences("USER", Context.MODE_PRIVATE)
					settings.edit().clear().apply()
				}
				.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
					dialog.dismiss()
				}
			val alert = builder.create()
			alert.show()
		}

		binding.textViewEmail.text = viewModel.auth.currentUser?.email
		binding.textViewCreationTime.text = viewModel.convertTimestampToFormattedDate(viewModel.auth.currentUser?.metadata?.creationTimestamp)

		val accountAdapter = AccountViewPagerAdapter {
			viewModel.navigateToEditAccount(it)
		}
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
		viewModel.fullAccountsFlow.onEach {
			accountAdapter.initList(it)
		}.launchIn(scope)

		val categoryOnClick = object : CategoryClickListener {
			override fun onClick(categoryItem: CategoryItem?) {
				if (categoryItem == null) return
				val builder = MaterialAlertDialogBuilder(requireContext())
				val redColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
				val typedValue = TypedValue()
				val theme = requireContext().theme
				theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
				val colorPrimary = typedValue.data
				val mes = getString(R.string.if_deleted_after)
				val message = SpannableStringBuilder()
					.append(getString(R.string.if_deleted))
					.append(" ")
					.bold {
						append(categoryItem.name)
						setSpan(ForegroundColorSpan(colorPrimary), length - categoryItem.name.length, length, 0)
					}
					.append(" ")
					.bold {
						append(mes)
						setSpan(ForegroundColorSpan(redColor), length - mes.length, length, 0)
					}
					.append()

				builder.setMessage(message)
					.setTitle(getString(R.string.confirm_dialog))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.ok)) { _, _ ->
						viewModel.deleteCategory(categoryItem.toCategory())
					}
					.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
						dialog.dismiss()
					}
				val alert = builder.create()
				alert.show()
			}
		}
		val categoryEarnAdapter = CategoryAdapter(categoryOnClick)
		binding.rvCategoriesEarning.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
		binding.rvCategoriesEarning.adapter = categoryEarnAdapter
		val categorySpendAdapter = CategoryAdapter(categoryOnClick)
		binding.rvCategoriesSpend.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
		binding.rvCategoriesSpend.adapter = categorySpendAdapter


		viewModel.categoriesFlow.onEach {
			val (spend, earn) = viewModel.categoriesFlow.value.toCategoryItem().partition { !it.isEarning }
			categoryEarnAdapter.setData(earn)
			categorySpendAdapter.setData(spend)
		}.launchIn(scope)
	}

}