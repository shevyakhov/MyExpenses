package com.chelz.features.accountedit.presentation

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.chelz.features.accountedit.R
import com.chelz.features.accountedit.databinding.FragmentEditAccountBinding
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.SharedAccountItem
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class EditAccountFragment : Fragment() {

	companion object {

		private const val ACCOUNT = "ACCOUNT"
		fun newInstance(accountItem: AccountItem): Fragment = EditAccountFragment().apply {
			arguments = bundleOf(
				ACCOUNT to accountItem
			)
		}
	}

	private var _binding: FragmentEditAccountBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<EditAccountViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentEditAccountBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val scope = viewLifecycleOwner.lifecycleScope

		val account: AccountItem? = arguments?.serializable(ACCOUNT)
		binding.cardName.setText(account?.name.toString())
		binding.cardBalance.setText(account?.money.toString())
		binding.cardNumber.setText(account?.number.toString())
		binding.isSharedCheckBox.isChecked = account is SharedAccountItem
		viewModel.currentAccountFlow.value = account

		viewModel.usersEmails.onEach {
			binding.usersChipGroup.removeAllViews()
			it.forEach { email ->
				if (!viewModel.guestFlow.value) {
					addHostChip(email)
				} else
					if (email == viewModel.auth.currentUser?.email) {
						addHostChip(email)
					} else {
						addQuestChip(email)
					}
			}
		}.launchIn(scope)

		bindData(scope)

		binding.buttonSave.setOnClickListener {
			val emails = binding.usersChipGroup.children.map { (it as TextView).text.toString() }
			viewModel.usersEmails.value = emails.toList().reversed()
			viewModel.saveAccount()
		}
		setDeleteButtonVisibility(account)

		binding.deleteButton.setOnClickListener {
			val builder = AlertDialog.Builder(requireContext())
			builder.setMessage(getString(R.string.confirm_dialog))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.ok)) { _, _ ->
					viewModel.deleteAccount()
				}
				.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
					dialog.dismiss()
				}
			val alert = builder.create()
			alert.show()
		}
		binding.buttonColor.setOnClickListener {
			showColorPickerDialog()
		}
	}

	private fun setDeleteButtonVisibility(account: AccountItem?) {
		if (account !is SharedAccountItem) {
			binding.deleteButton.isVisible = true
		} else {
			viewModel.guestFlow.onEach {
				binding.deleteButton.isVisible = !it
			}.launchIn(scope = viewLifecycleOwner.lifecycleScope)
		}
	}

	private fun addQuestChip(email: String) {
		val chip = createChip(requireContext(), email)
		binding.usersChipGroup.addView(chip, 0)
		chip.isClickable = false
		chip.isCloseIconVisible = false
		chip.isEnabled = false
		chip.setHintTextColor(Color.GRAY)
	}

	private fun bindData(scope: LifecycleCoroutineScope) {
		viewModel.error.onEach {
			Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
		}.launchIn(scope)

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

		viewModel.hostFlow.onEach {
			binding.addChip.isVisible = it == viewModel.auth.currentUser?.email
			binding.usersChipGroup.isClickable = it == viewModel.auth.currentUser?.email
		}.launchIn(scope)
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
				getString(R.string.ok),
				ColorEnvelopeListener { envelope, _ ->
					viewModel.chosenColorFlow.value = "#${envelope.hexCode}"
				})
			.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
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
			addHostChip(input.text.toString())
		}
		builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
		builder.show()
	}

	private fun addHostChip(email: String) {
		val chip = createChip(requireContext(), email)
		binding.usersChipGroup.addView(chip, 0)
		chip.isChecked = true
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

	private inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
		Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
		else                                                  -> @Suppress("DEPRECATION") getSerializable(key) as? T
	}
}