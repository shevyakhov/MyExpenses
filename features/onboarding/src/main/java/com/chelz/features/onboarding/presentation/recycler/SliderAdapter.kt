package com.chelz.features.onboarding.presentation.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.chelz.features.onboarding.R
import com.chelz.features.onboarding.databinding.SliderItemBinding

class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderHolder>() {

	private var sliderList = listOf<SliderItem>()

	class SliderHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val sliderItemBinding = SliderItemBinding.bind(view)
		private val context: Context = view.context
		fun bind(item: SliderItem) = with(sliderItemBinding) {
			sliderMainText.text = context.getText(item.mainText)
			sliderSubText.text = context.getText(item.subText)
			animationView.setAnimation(item.animationRaw)
			animationView.imageAssetsFolder = "raw/"
			animationView.repeatCount = when (item.animationMode) {
				LottieDrawable.INFINITE -> LottieDrawable.INFINITE
				LottieDrawable.REVERSE  -> 5
				else                    -> 0
			}
			animationView.repeatMode = item.animationMode
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_item, parent, false)
		return SliderHolder(view)
	}

	override fun onBindViewHolder(holder: SliderHolder, position: Int) {
		holder.bind(sliderList[position])
	}

	override fun getItemCount(): Int {
		return sliderList.size
	}

	fun initList(list: List<SliderItem>) {
		sliderList = list
	}
}