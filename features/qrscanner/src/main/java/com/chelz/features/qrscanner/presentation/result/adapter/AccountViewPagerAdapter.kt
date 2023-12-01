package com.chelz.features.qrscanner.presentation.result.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import com.chelz.features.qrscanner.R
import com.chelz.features.qrscanner.databinding.AccountItemBinding

class AccountViewPagerAdapter : RecyclerView.Adapter<AccountViewPagerAdapter.SliderHolder>() {

	private var sliderList = listOf<AccountItem>()

	class SliderHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val sliderItemBinding = AccountItemBinding.bind(view)
		fun bind(item: AccountItem) = with(sliderItemBinding) {
			accountName.text = item.name
			number.text = item.number
			balance.text = item.money.toString()
			val color = Color.parseColor(item.color)
			root.setCardBackgroundColor(color)
			val r = 255 - color.red
			val g = 255 - color.green
			val b = 255 - color.blue
			val new = Color.rgb(r, g, b)
			accountName.setTextColor(new)
			number.setTextColor(new)
			numberTitle.setTextColor(new)
			balanceTitle.setTextColor(new)
			balance.setTextColor(new)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.account_item, parent, false)
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