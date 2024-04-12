package com.chelz.features.planning.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chelz.features.planning.databinding.CategoryCaruselItemBinding
import com.chelz.shared.accounts.domain.entity.CategoryItem

class CategoryPlanningAdapter :
	RecyclerView.Adapter<CategoryPlanningAdapter.CategoryHolder>() {

	private var categories = mutableListOf<CategoryItem>()

	inner class CategoryHolder(
		private val binding: CategoryCaruselItemBinding,
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(item: CategoryItem) {
			with(binding) {
				categoryLetter.text = item.name.first().toString()
				category.text = item.name
				val color = Color.parseColor(item.color)
				val brightness = (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114) / 255

				val textColor = if (brightness > 0.5) root.context.getColor(com.chelz.libraries.theme.R.color.neutral_1) else Color.WHITE

				categoryLetter.setTextColor(textColor)
				background.setCardBackgroundColor(color)
			}
		}
	}

	@SuppressLint("NotifyDataSetChanged")
	fun setData(list: List<CategoryItem>) {
		categories = list.toMutableList()
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
		val binding = CategoryCaruselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return CategoryHolder(binding)
	}

	override fun getItemCount(): Int =
		categories.size

	override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
		holder.bind(categories[position])
	}

	fun getItemAt(position: Int): CategoryItem {
		return categories.get(position)
	}
}