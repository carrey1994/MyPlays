package com.example.myplays

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
	
	private val _videoData = MutableLiveData<Uri>()
	val videoData = _videoData
	
	private val _imageIds = MutableLiveData<List<Long>>()
	val imageIds = _imageIds
	
	//Animation for removing selected image
	fun startGoneAnim(view: View, button: View, fn: () -> Unit) {
		val scaleX = ObjectAnimator.ofFloat(view, "ScaleX", 1f, 0f).apply { duration = 500 }
		val scaleY = ObjectAnimator.ofFloat(view, "ScaleY", 1f, 0f).apply { duration = 500 }
		AnimatorSet().run {
			playTogether(scaleX, scaleY)
			doOnStart { button.isClickable = false }
			doOnEnd { fn() }
			start()
		}
	}
	
	//Search image from device
	//Bind image Id and its own uri together in map
	@SuppressLint("Recycle")
	fun searchImages(contentResolver: ContentResolver): HashMap<Boolean, List<*>>? {
		val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null) ?: return null
		val imagesData = arrayListOf<Uri>()
		val imageIds = arrayListOf<Long>()
		while (cursor.moveToNext()) {
			val index = cursor.getColumnIndex(MediaStore.Images.Media._ID)
			val id = cursor.getLong(index)
			val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
			imagesData.add(imageUri)
			imageIds.add(id)
		}
		cursor.close()
		val hashMap = HashMap<Boolean, List<*>>()
		hashMap[true] = imagesData
		hashMap[false] = imageIds
		return hashMap
	}
	
	
}