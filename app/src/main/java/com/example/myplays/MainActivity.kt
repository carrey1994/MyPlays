package com.example.myplays

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore.Images.Media.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class MainActivity : AppCompatActivity() {
	
	private val mainViewModel by lazy {
		ViewModelProviders.of(this).get(MainViewModel::class.java)
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setupViewsListeners()
		observeVideoData()
		observeImageIds()
	}
	
	
	private fun setupViewsListeners() {
		btn_video.setOnClickListener {
			val videoIntent = Intent()
			videoIntent.type = "video/*"
			videoIntent.action = Intent.ACTION_GET_CONTENT
			mainViewModel.videoData.postValue(null)
			startActivityForResult(Intent.createChooser(videoIntent, "Get Video"), REQUEST_TAKE_GALLERY_VIDEO)
		}
		
		btn_photo.setOnClickListener {
			val permission = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
			if (permission != PackageManager.PERMISSION_GRANTED) {
				//Request for Permission
				ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), REQUEST_TAKE_GALLERY_PHOTO)
			} else {
				waitSearchImages()
			}
		}
	}
	
	//Go to pick images and clear selected images, removeIds
	private fun waitSearchImages() {
		val mIntent = Intent(this, PickActivity::class.java)
		val hashMap = mainViewModel.searchImages(contentResolver)
		mIntent.putExtra(SEARCH_DATA, hashMap)
		mainViewModel.removeIds.clear()
		ll_photos.removeAllViews()
		startActivityForResult(mIntent, RESULT_PHOTO_CODE)
	}
	
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		when (requestCode) {
			REQUEST_TAKE_GALLERY_PHOTO -> {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					waitSearchImages()
				}
			}
		}
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (resultCode) {
			//Get the photo and add views
			RESULT_PHOTO_CODE -> {
				val imageIds = data?.extras!![IMAGES_ID] as List<Long>
				mainViewModel.imageIds.postValue(imageIds)
			}
		}
		when (requestCode) {
			//Get the video and set video data
			REQUEST_TAKE_GALLERY_VIDEO -> {
				mainViewModel.videoData.postValue(data?.data)
			}
		}
		
	}
	
	//If get the video, set data to video view
	private fun observeVideoData() = mainViewModel.videoData.observe(this, Observer {
		if (it == null) {
			video_view.visibility = View.GONE
			video_view.resetVideo()
			return@Observer
		}
		video_view.requestFocus()
		video_view.setVideoData(it)
		video_view.setRemoveFun {
			mainViewModel.videoData.postValue(null)
		}
	})
	
	
	//If get the imageIds, generate views and add them into horizontal layout
	private fun observeImageIds() = mainViewModel.imageIds.observe(this, Observer {
		it.forEach { id ->
			if (mainViewModel.removeIds.contains(id)) return@forEach
			val view = LayoutInflater.from(this).inflate(R.layout.layout_photo, ll_photos as ViewGroup, false)
			val imageUri = ContentUris.withAppendedId(EXTERNAL_CONTENT_URI, id)
			view.findViewById<ImageView>(R.id.si_photo).setImageURI(imageUri)
			view.findViewById<ImageView>(R.id.iv_remove).setOnClickListener { btn ->
				mainViewModel.startGoneAnim(view, btn, id) { ll_photos.removeView(view) }
			}
			ll_photos.addView(view)
		}
	})
	
}
