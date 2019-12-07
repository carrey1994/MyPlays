package com.example.myplays

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

//Binding data to item_photo
object PhotoBindings {
	
	//For make item is select or not and change selector
	@JvmStatic
	@BindingAdapter("selectShow", requireAll = true)
	fun setSelectIc(view: View, isSelect: Boolean) {
		view.isSelected = isSelect
	}
	
	//To set bitmap into the ImageView
	@JvmStatic
	@BindingAdapter("photoBitmap")
	fun setPhotoBitmap(view: ImageView, bitmap: Bitmap) {
		view.setImageBitmap(bitmap)
	}
	
}