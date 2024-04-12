package com.chelz.features.statistics.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chelz.features.statistics.R
import com.chelz.features.statistics.databinding.StatsAccountItemBinding
import com.chelz.features.statistics.databinding.StatsCategoryItemBinding
import com.chelz.shared.accounts.domain.entity.AccountItem
import com.chelz.shared.accounts.domain.entity.CategoryItem

class SwipeItemAdapter :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private var items: MutableList<Any> = mutableListOf()

	companion object {

		private const val VIEW_TYPE_ACCOUNT = 0
		private const val VIEW_TYPE_CATEGORY = 1
	}

	fun updateAccount(value: List<AccountItem>) {
		items.clear()
		items.addAll(value)
		notifyDataSetChanged()
	}

	fun updateCategory(value: List<CategoryItem>) {
		items.clear()
		items.addAll(value)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return when (viewType) {
			VIEW_TYPE_ACCOUNT  -> {
				val binding = StatsAccountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
				AccountViewHolder(binding)
			}

			VIEW_TYPE_CATEGORY -> {
				val binding = StatsCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
				CategoryViewHolder(binding)
			}

			else               -> throw IllegalArgumentException("Invalid view type")
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder) {
			is AccountViewHolder  -> {
				val item = items[position] as AccountItem
				holder.bind(item)
			}

			is CategoryViewHolder -> {
				val item = items[position] as CategoryItem
				holder.bind(item)
			}
		}
	}

	override fun getItemCount(): Int = items.size

	override fun getItemViewType(position: Int): Int {
		return when (items[position]) {
			is AccountItem  -> VIEW_TYPE_ACCOUNT
			is CategoryItem -> VIEW_TYPE_CATEGORY
			else            -> throw IllegalArgumentException("Invalid data at position $position")
		}
	}

	fun getItemAt(position: Int): Any {
		return items[position]
	}

	inner class AccountViewHolder(private val binding: StatsAccountItemBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bind(accountItem: AccountItem) {
			with(binding) {
				accountName.text = accountItem.name
				number.text = accountItem.number
				val moneyFormatted = String.format("%.2f", accountItem.money).replace(',', '.')
				balance.text = moneyFormatted
				val color = Color.parseColor(accountItem.color)
				root.setCardBackgroundColor(color)

				val brightness = (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114) / 255

				val textColor = if (brightness > 0.5) root.context.getColor(com.chelz.libraries.theme.R.color.neutral_1) else Color.WHITE
				accountName.setTextColor(textColor)
				number.setTextColor(textColor)
				numberTitle.setTextColor(textColor)
				balanceTitle.setTextColor(textColor)
				balance.setTextColor(textColor)
			}
		}
	}

	inner class CategoryViewHolder(private val binding: StatsCategoryItemBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bind(categoryItem: CategoryItem) {
			binding.apply {
				categoryLetter.text = categoryItem.name.first().toString()
				category.text = categoryItem.name
				val color = Color.parseColor(categoryItem.color)
				background.setCardBackgroundColor(color)
				val brightness = (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114) / 255

				val textColor = if (brightness > 0.5) root.context.getColor(com.chelz.libraries.theme.R.color.neutral_1) else Color.WHITE
				categoryLetter.setTextColor(textColor)
				category.setTextColor(textColor)
				isEarning.setTextColor(textColor)
				isEarning.text = if (categoryItem.isEarning) root.context.getString(R.string.earnings) else root.context.getString(R.string.spend)
			}
		}
	}
}