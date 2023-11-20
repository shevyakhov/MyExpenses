package com.chelz.features.categoryadd.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.chelz.features.categoryadd.R
import com.chelz.features.categoryadd.databinding.FragmentAddCategoryBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCategoryFragment : Fragment() {

	private var _binding: FragmentAddCategoryBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<AddCategoryViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope

		bindData(scope)

		binding.buttonAdd.setOnClickListener {
			viewModel.saveCategory()
		}
		binding.buttonColor.setOnClickListener {
			showColorPickerDialog()
		}
	}

	private fun bindData(scope: LifecycleCoroutineScope) {
		viewModel.chosenColorFlow.onEach {
			val color = Color.parseColor(it)
			binding.background.setCardBackgroundColor(color)

			val r = 255 - color.red
			val g = 255 - color.green
			val b = 255 - color.blue
			val textColor = Color.rgb(r, g, b)
			binding.colorChosen.text = it
			binding.categoryLetter.setTextColor(textColor)
		}.launchIn(scope)

		viewModel.nameFlow.onEach {
			binding.category.text = it
			binding.categoryNameLayout.error = bindErrorLayout(it.isNotEmpty())
			binding.categoryLetter.text = (it.firstOrNull() ?: "?").toString().uppercase()
		}.launchIn(scope)

		binding.isEarningCheckBox.setOnCheckedChangeListener { _, b ->
			viewModel.isEarningFlow.value = b
		}

		binding.categoryName.doAfterTextChanged {
			viewModel.nameFlow.value = it.toString()
		}
	}

	private fun bindErrorLayout(notEmpty: Boolean): String? {
		return if (notEmpty) {
			null
		} else
			getString(R.string.empty_value)
	}

	private fun showColorPickerDialog() {
		val dialog = ColorPickerDialog.Builder(requireContext())
			.setTitle(getString(R.string.choose_color))
			.setPositiveButton(
				getString(R.string.ok),
				ColorEnvelopeListener { envelope, _ ->
					viewModel.chosenColorFlow.value = "#${envelope.hexCode}"
				})
			.setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
			.attachBrightnessSlideBar(true)
			.attachAlphaSlideBar(false)
		dialog.show()
	}
}