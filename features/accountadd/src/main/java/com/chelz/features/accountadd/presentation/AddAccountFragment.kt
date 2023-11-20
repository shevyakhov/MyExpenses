package com.chelz.features.accountadd.presentation

import android.R.string
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
import com.chelz.features.accountadd.R
import com.chelz.features.accountadd.databinding.FragmentAddAccountBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAccountFragment : Fragment() {

	private var _binding: FragmentAddAccountBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<AddAccountViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentAddAccountBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope

		bindData(scope)

		binding.buttonAdd.setOnClickListener {
			viewModel.saveAccount()
		}
		binding.buttonColor.setOnClickListener {
			showColorPickerDialog()
		}
	}

	private fun bindData(scope: LifecycleCoroutineScope) {
		viewModel.chosenColorFlow.onEach {
			val color = Color.parseColor(it)
			binding.accountLayout.setCardBackgroundColor(color)

			val r = 255 - color.red
			val g = 255 - color.green
			val b = 255 - color.blue
			val textColor = Color.rgb(r, g, b)
			binding.accountName.setTextColor(textColor)
			binding.balance.setTextColor(textColor)
			binding.balanceTitle.setTextColor(textColor)
			binding.numberTitle.setTextColor(textColor)
			binding.number.setTextColor(textColor)
			binding.colorChosen.text = it
		}.launchIn(scope)

		viewModel.nameFlow.onEach {
			binding.accountName.text = it
			binding.cardNameLayout.error = bindErrorLayout(it.isNotEmpty())
		}.launchIn(scope)
		viewModel.balanceFlow.onEach {
			binding.balance.text = it.toString()
		}.launchIn(scope)

		viewModel.cardNumberFlow.onEach {
			binding.number.text = it
		}.launchIn(scope)

		binding.cardBalance.doAfterTextChanged {
			viewModel.balanceFlow.value = it.toString().toDoubleOrNull() ?: 0.0
		}
		binding.cardName.doAfterTextChanged {
			viewModel.nameFlow.value = it.toString()
		}

		binding.cardNumber.doAfterTextChanged {
			viewModel.cardNumberFlow.value = it.toString()
		}
		binding.isSharedCheckBox.setOnCheckedChangeListener { _, b ->
			viewModel.isSharedFlow.value = b
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
				getString(string.ok),
				ColorEnvelopeListener { envelope, _ ->
					viewModel.chosenColorFlow.value = "#${envelope.hexCode}"
				})
			.setNegativeButton(getString(string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
			.attachBrightnessSlideBar(true)
			.attachAlphaSlideBar(false)
		dialog.show()
	}
}
