package com.example.myplays.adapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter


//Binding data to item_radio_data
object RadioDataBinding {
	
	//For make item is select or not and change selector
	@JvmStatic
	@BindingAdapter("selectRadioShow", requireAll = true)
	fun isRadioSelect(view: View, isSelect: Boolean) {
		view.isSelected = isSelect
	}
	
	@JvmStatic
	@BindingAdapter("radioIdText", requireAll = true)
	fun radioIdText(view: TextView, id: Int) {
		view.text = id.toString()
	}
	
}