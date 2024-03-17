package com.chelz.features.home.presentation.recycler.operations

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chelz.features.home.databinding.OperationItemBinding
import com.chelz.shared.accounts.domain.entity.OperationItem

class OperationAdapter : RecyclerView.Adapter<OperationAdapter.OperationHolder>() {

	var operations = mutableListOf<OperationItem>()
	fun setNewData(newData: List<OperationItem>) {
		operations.clear()
		operations.addAll(newData)
		notifyDataSetChanged()
	}

	inner class OperationHolder(private val binding: OperationItemBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(item: OperationItem) {
			with(binding) {
				categoryLetter.text = (item.category?.name?.first() ?: "?").toString()
				category.text = item.name ?: item.category?.name ?: "?"
				accountName.text = item.account
				money.text = buildString {
					append(item.quantity.toString())
					append(END_SYMBOL)
				}
				val color = if (item.quantity > 0.0) {
					root.context.getColor(com.chelz.libraries.theme.R.color.semantic_cyan)
				} else {
					root.context.getColor(com.chelz.libraries.theme.R.color.semantic_red)
				}
				val itemColor = Color.parseColor(item.category?.color ?: "#FFFBBB")
				background.setCardBackgroundColor(itemColor)

				val brightness = (Color.red(itemColor) * 0.299 + Color.green(itemColor) * 0.587 + Color.blue(itemColor) * 0.114) / 255

				val textColor = if (brightness > 0.5) root.context.getColor(com.chelz.libraries.theme.R.color.neutral_1) else Color.WHITE

				money.setTextColor(color)
				categoryLetter.setTextColor(textColor)
			}
		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationHolder {
		val binding = OperationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return OperationHolder(binding)
	}

	override fun getItemCount(): Int = operations.size

	override fun onBindViewHolder(holder: OperationHolder, position: Int) {
		holder.bind(operations[position])
		holder.setIsRecyclable(false)
	}

	companion object {

		const val END_SYMBOL = "₽"
	}
}
