package com.example.myplays

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class PickViewModel : BaseViewModel() {
	
	private val _photoLiveData = MutableLiveData<List<PhotoModel>>()
	val photoLiveData = _photoLiveData
	
	private val _pickPhotos = arrayListOf<Long>()
	val pickPhotos = _pickPhotos
	
	@SuppressLint("CheckResult")
	fun dealData(searchData: HashMap<Boolean, *>, contentResolver: ContentResolver) {
		val data = searchData[true] as List<Uri>
		val ids = searchData[false] as List<Long>
		Single.create<List<Uri>> { data }
			.subscribeOn(Schedulers.io())
			.map {
				it.map {
					val i = data.indexOf(it)
					val orinBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data[i]))
					val bitmap = Bitmap.createScaledBitmap(orinBitmap, (orinBitmap.width / 3), (orinBitmap.height / 3), false)
					PhotoModel(bitmap, ids[i])
				}
			}
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				{ result ->
					_photoLiveData.postValue(result)
				},
				{ error ->
					Log.e("Error====>", "${error.message}")
				}
			).addTo(compositeDisposable)
	}
	
	
	@SuppressLint("Recycle")
	fun searchImages(contentResolver: ContentResolver) {
		val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
		val imagesData = arrayListOf<Uri>()
		val imageIds = arrayListOf<Long>()
		while (cursor!!.moveToNext()) {
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
		dealData(hashMap, contentResolver)
	}
	
}