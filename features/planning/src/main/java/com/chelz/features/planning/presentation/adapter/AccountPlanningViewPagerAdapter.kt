package com.chelz.features.planning.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chelz.features.planning.R
import com.chelz.features.planning.databinding.AccountPlanItemBinding
import com.chelz.shared.accounts.domain.entity.AccountItem

class AccountPlanningViewPagerAdapter : RecyclerView.Adapter<AccountPlanningViewPagerAdapter.SliderHolder>() {

	private var sliderList = listOf<AccountItem>()

	inner class SliderHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val sliderItemBinding = AccountPlanItemBinding.bind(view)
		fun bind(item: AccountItem) = with(sliderItemBinding) {
			accountName.text = item.name
			number.text = item.number
			val moneyFormatted = String.format("%.2f", item.money).replace(',', '.')
			balance.text = moneyFormatted
			val color = Color.parseColor(item.color)
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

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.account_plan_item, parent, false)
		return SliderHolder(view)
	}

	override fun onBindViewHolder(holder: SliderHolder, position: Int) {
		holder.bind(sliderList[position])
	}

	override fun getItemCount(): Int {
		return sliderList.size
	}

	fun getItemAt(position: Int): AccountItem = sliderList[position]

	fun initList(list: List<AccountItem>) {
		sliderList = list
		notifyDataSetChanged()
	}
}