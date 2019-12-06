package com.example.myplays

import android.graphics.Bitmap
import java.io.Serializable

data class PhotoModel(
	val bitmap: Bitmap,
	val index: Long,
	var isCheck: Boolean = false
) : Serializable