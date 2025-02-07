package com.chelz.features.profile.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.chelz.features.profile.databinding.CategoryItemBinding
import com.chelz.shared.accounts.domain.entity.CategoryItem

class CategoryAdapter(private val onClickListener: CategoryClickListener) :
	RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

	private var categories = mutableListOf<CategoryItem>()
	private var selectedItem: CategoryItem? = null

	inner class CategoryHolder(
		private val binding: CategoryItemBinding,
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(item: CategoryItem) {
			with(binding) {
				categoryLetter.text = item.name.first().toString()
				category.text = item.name
				val color = Color.parseColor(item.color)
				background.setCardBackgroundColor(color)
				val brightness = (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114) / 255

				val textColor = if (brightness > 0.5) root.context.getColor(com.chelz.libraries.theme.R.color.neutral_1) else Color.WHITE
				categoryLetter.setTextColor(textColor)
				onChosen.isVisible = item == selectedItem
				category.setTextColor(Color.BLACK)
				root.setOnClickListener {
					handleItemClick(item)
				}
			}
		}
	}

	@SuppressLint("NotifyDataSetChanged")
	fun setData(list: List<CategoryItem>) {
		categories = list.toMutableList()
		notifyDataSetChanged()
	}

	private fun handleItemClick(item: CategoryItem) {
		if (item == selectedItem) {
			val newPosition = categories.indexOf(item)
			selectedItem = null
			notifyItemChanged(newPosition)
		} else {
			val previousSelectedItem = selectedItem
			selectedItem = item
			val oldPosition = categories.indexOf(previousSelectedItem)
			val newPosition = categories.indexOf(item)
			notifyItemChanged(oldPosition)
			notifyItemChanged(newPosition)
		}
		onClickListener.onClick(selectedItem)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
		val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return CategoryHolder(binding)
	}

	override fun getItemCount(): Int =
		categories.size

	override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
		holder.bind(categories[position])
	}
}