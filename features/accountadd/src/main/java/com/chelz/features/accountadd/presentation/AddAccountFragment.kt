package com.chelz.features.accountadd.presentation

import android.R.string
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.chelz.features.accountadd.R
import com.chelz.features.accountadd.databinding.FragmentAddAccountBinding
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

		binding.buttonSave.setOnClickListener {
			val emails = binding.usersChipGroup.children.map { (it as TextView).text.toString() }
			viewModel.usersEmails.value = emails.toList()
			viewModel.saveAccount()
		}
		binding.buttonColor.setOnClickListener {
			showColorPickerDialog()
		}
	}

	private fun bindData(scope: LifecycleCoroutineScope) {
		viewModel.error.onEach {
			Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
		}.launchIn(scope)

		viewModel.chosenColorFlow.onEach {
			val color = Color.parseColor(it)
			binding.accountLayout.setCardBackgroundColor(color)

			val brightness = (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114) / 255

			val textColor = if (brightness > 0.5) requireContext().getColor(com.chelz.libraries.theme.R.color.neutral_1) else Color.WHITE
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
		binding.addChip.setOnClickListener {
			addChip()
		}
		viewModel.isSharedFlow.onEach {
			binding.usersScroll.visibility = if (it) View.VISIBLE else View.GONE
			binding.usersText.visibility = if (it) View.VISIBLE else View.GONE
		}.launchIn(scope)
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

	private fun addChip() {
		val builder = MaterialAlertDialogBuilder(requireContext())
		builder.setTitle("Пригласить пользователя")
		val input = EditText(requireContext())
		input.hint = "email пользователя"
		input.inputType = InputType.TYPE_CLASS_TEXT
		builder.setView(input)

		builder.setPositiveButton(getString(R.string.add)) { _, _ ->
			val chip = createChip(requireContext(), input.text.toString())
			binding.usersChipGroup.addView(chip, 0)
			chip.isChecked = true
		}
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
		builder.show()
	}

	private fun createChip(context: Context, chipName: String): Chip {
		return Chip(context).apply {
			text = chipName
			isCheckable = false
			isCloseIconVisible = true
			isCheckedIconVisible = true
			setOnCloseIconClickListener {
				binding.usersChipGroup.removeView(this)
			}
		}
	}
}
