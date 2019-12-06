package com.example.myplays

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable

class PickViewModel : ViewModel() {
	
	val _photoLiveData = MutableLiveData<List<PhotoModel>>()
	val photoLiveData = _photoLiveData
	
	private val _pickPhotos = arrayListOf<Long>()
	val pickPhotos = _pickPhotos
	
	fun dealData(searchData: HashMap<Boolean, *>, contentResolver: ContentResolver) {
		val data = searchData[true] as List<Uri>
		val ids = searchData[false] as List<Long>
		val photos = arrayListOf<PhotoModel>()
		for (i in data.indices) {
			val orinBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data[i]))
			val bitmap = Bitmap.createScaledBitmap(orinBitmap, (orinBitmap.width / 3), (orinBitmap.height / 3), false)
			photos.add(PhotoModel(bitmap, ids[i]))
		}
		_photoLiveData.postValue(photos)
	}
	
}