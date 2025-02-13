package com.chelz.features.qrscanner.presentation.result.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chelz.features.qrscanner.databinding.QrListItemBinding
import com.chelz.features.qrscanner.domain.entity.ItemsEntity
import com.chelz.shared.accounts.domain.entity.Category
import android.R as AndroidR

class QrListAdapter(
	private var categories: List<Category>,
) : ListAdapter<QrItem, QrListAdapter.Holder>(FoodListDiffCallback) {

	inner class Holder(
		private val binding: QrListItemBinding,
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(item: QrItem) {

			with(binding) {
				bindCategory(item)
				val moneyFormatted = String.format("%.2f", (item.quantity!! * item.price!! / 100)).replace(',', '.')
				money.text = buildString {
					append(moneyFormatted).toString()
					append(END_SYMBOL)
				}

				val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(root.context, AndroidR.layout.simple_spinner_item, categories.map { it.name })
				arrayAdapter.setDropDownViewResource(AndroidR.layout.simple_spinner_dropdown_item)
				spinner.adapter = arrayAdapter

				spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
					override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
						(view as TextView).text = null
						item.category = categories[position]
						currentList[adapterPosition].category = categories[position]
						bindCategory(item)
					}

					override fun onNothingSelected(parent: AdapterView<*>?) {
						currentList[adapterPosition].category = null
						item.category = null
					}
				}
			}
		}

		private fun QrListItemBinding.bindCategory(item: QrItem) {
			if (item.category != fakeCategory) {
				categoryLetter.text = (item.category?.name?.first() ?: "?").toString()
				category.text = item.name ?: item.category?.name ?: "?"

				val itemColor = Color.parseColor(item.category?.color ?: "#FFFCDE")
				background.setCardBackgroundColor(itemColor)

				val brightness = (Color.red(itemColor) * 0.299 + Color.green(itemColor) * 0.587 + Color.blue(itemColor) * 0.114) / 255
				val textColor = if (brightness > 0.5) root.context.getColor(com.chelz.libraries.theme.R.color.neutral_1) else Color.WHITE
				categoryLetter.setTextColor(textColor)
			} else {
				categoryLetter.text = "?"
				category.text = item.name ?: "?"
				val itemColor = Color.parseColor("#FFFCDE")
				background.setCardBackgroundColor(itemColor)
				val brightness = (Color.red(itemColor) * 0.299 + Color.green(itemColor) * 0.587 + Color.blue(itemColor) * 0.114) / 255
				val textColor = if (brightness > 0.5) root.context.getColor(com.chelz.libraries.theme.R.color.neutral_1) else Color.WHITE
				categoryLetter.setTextColor(textColor)
			}
			spinner.setBackgroundColor(Color.parseColor("#00000000"))
			spinner.setBackgroundColor(Color.parseColor("#00000000"))
		}
	}

	fun updateData(newData: ItemsEntity?) {
		submitList(newData?.toQrItem() ?: emptyList())
	}

	fun updateQrData(newData: List<QrItem>) {
		submitList(newData)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
		val view = LayoutInflater.from(parent.context)
		val binding = QrListItemBinding.inflate(view, parent, false)
		return Holder(binding)
	}

	override fun onBindViewHolder(holder: Holder, position: Int) {
		holder.bind(getItem(position))
	}

	fun redrawWithCategories(it: List<Category>) {
		categories = it
		submitList(currentList)
		notifyDataSetChanged()
	}

	private object FoodListDiffCallback : DiffUtil.ItemCallback<QrItem>() {

		override fun areItemsTheSame(oldItem: QrItem, newItem: QrItem) = oldItem.name == newItem.name

		override fun areContentsTheSame(oldItem: QrItem, newItem: QrItem) = oldItem == newItem
	}

	companion object {

		val fakeCategory = Category(-1, "Ничего")

		const val END_SYMBOL = "₽"
	}
}