package com.chelz.features.planning.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
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
				val r = 255 - color.red
				val g = 255 - color.green
				val b = 255 - color.blue
				val new = Color.rgb(r, g, b)

				categoryLetter.setTextColor(new)
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