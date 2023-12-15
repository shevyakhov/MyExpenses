package com.chelz.features.home.presentation.recycler.operations

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import com.chelz.features.home.databinding.OperationItemBinding
import com.chelz.libraries.theme.getThemeColor
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
					getThemeColor(root.context, com.google.android.material.R.attr.colorPrimary)
				} else {
					getThemeColor(root.context, com.google.android.material.R.attr.colorError)
				}
				val itemColor = Color.parseColor(item.category?.color ?: "#FFFBBB")
				background.setCardBackgroundColor(itemColor)
				category.setTextColor(color)
				money.setTextColor(color)

				val r = 255 - itemColor.red
				val g = 255 - itemColor.green
				val b = 255 - itemColor.blue
				val new = Color.rgb(r, g, b)
				categoryLetter.setTextColor(new)
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
