package com.chelz.features.qrscanner.presentation.result

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.chelz.features.qrscanner.R
import com.chelz.features.qrscanner.databinding.FragmentQrResultBinding
import com.chelz.features.qrscanner.domain.entity.HtmlEntity
import com.chelz.features.qrscanner.domain.entity.ItemsEntity
import com.chelz.features.qrscanner.presentation.result.adapter.AccountViewPagerAdapter
import com.chelz.features.qrscanner.presentation.result.adapter.HorizontalMarginItemDecoration
import com.chelz.features.qrscanner.presentation.result.adapter.QrListAdapter
import com.chelz.features.qrscanner.presentation.result.adapter.toQrItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class QrResultFragment : Fragment() {

	private var _binding: FragmentQrResultBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<QrResultViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentQrResultBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope

		viewModel.init()
		val items: ItemsEntity? = arguments?.serializable(ITEMS)
		val html: HtmlEntity? = arguments?.serializable(HTML)

		viewModel.initCategoriesSize(items?.toQrItem() ?: emptyList())
		val qrAdapter = QrListAdapter(categories = emptyList())
		qrAdapter.updateData(items)

		viewModel.categoriesFlow.onEach {
			qrAdapter.redrawWithCategories(it)
		}.launchIn(scope)

		viewModel.chosenCategories.onEach {
			qrAdapter.updateQrData(it)
		}.launchIn(scope)

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.recyclerView.adapter = qrAdapter

		Log.e("HTML", html?.html.toString())
		binding.webView.loadDataWithBaseURL(null, html?.html.toString(), "text/html", "utf-8", null)

		binding.saveButton.setOnClickListener {
			viewModel.saveOperations(qrAdapter.currentList)
		}
		bindAccounts(scope)
	}

	private fun bindAccounts(scope: LifecycleCoroutineScope) {
		val accountAdapter = AccountViewPagerAdapter()
		val itemDecoration = HorizontalMarginItemDecoration(binding.root.context, R.dimen.viewpager_current_item_horizontal_margin)

		binding.accountViewPager.adapter = accountAdapter
		binding.accountViewPager.apply {
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
		binding.accountViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				val account = accountAdapter.getItemAt(position)
				viewModel.currentAccount.value = account
			}
		})

		viewModel.fullAccountsFlow.onEach {
			accountAdapter.initList(it)
		}.launchIn(scope)
	}

	companion object {

		private const val ITEMS = "ITEMS"
		private const val HTML = "HTML"
		fun newInstance(items: ItemsEntity, htmlEntity: HtmlEntity): QrResultFragment =
			QrResultFragment().apply {
				arguments = bundleOf(
					ITEMS to items,
					HTML to htmlEntity
				)
			}
	}

	private inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
		Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
		else                                                  -> @Suppress("DEPRECATION") getSerializable(key) as? T
	}
}