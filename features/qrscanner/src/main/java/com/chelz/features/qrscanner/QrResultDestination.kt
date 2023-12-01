package com.chelz.features.qrscanner

import androidx.fragment.app.Fragment
import com.chelz.features.qrscanner.domain.entity.HtmlEntity
import com.chelz.features.qrscanner.domain.entity.ItemsEntity
import com.chelz.features.qrscanner.presentation.result.QrResultFragment
import com.chelz.libraries.navigation.FragmentDestination

class QrResultDestination(private val items: ItemsEntity, private val htmlEntity: HtmlEntity) : FragmentDestination {

	override fun createInstance(): Fragment =
		QrResultFragment.newInstance(
			items, htmlEntity
		)
}