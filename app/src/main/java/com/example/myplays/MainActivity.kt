package com.example.myplays

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
			startActivityForResult(Intent.createChooser(videoIntent, "Get Video"), REQUEST_TAKE_GALLERY_VIDEO)
		}
		
		btn_photo.setOnClickListener {
			val permission = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
			if (permission != PackageManager.PERMISSION_GRANTED) {
				//Request for Permission
				ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), REQUEST_TAKE_GALLERY_PHOTO)
			} else {
				//Wait and Go to pick images
				val mIntent = Intent(this, PickActivity::class.java)
				val hashMap = mainViewModel.searchImages(contentResolver)
				mIntent.putExtra(SEARCH_DATA, hashMap)
				startActivityForResult(mIntent, RESULT_PHOTO_CODE)
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
				mainViewModel.videoData.postValue(data!!.data)
			}
		}
		
	}
	
	//If get the video, set data to video view
	private fun observeVideoData() = mainViewModel.videoData.observe(this, Observer {
		cv_video.visibility = View.VISIBLE
		video_view.setVideoData(it)
	})
	
	
	//If get the imageIds, generate views to horizontal layout
	private fun observeImageIds() = mainViewModel.imageIds.observe(this, Observer {
		it.forEach { id ->
			val view = LayoutInflater.from(this).inflate(R.layout.layout_photo, ll_photos as ViewGroup, false)
			val imageUri = ContentUris.withAppendedId(EXTERNAL_CONTENT_URI, id)
			view.findViewById<ImageView>(R.id.si_photo).setImageURI(imageUri)
			view.findViewById<ImageView>(R.id.iv_remove).setOnClickListener {
				mainViewModel.startGoneAnim(view, it) { ll_photos.removeView(view) }
			}
			ll_photos.addView(view)
		}
	})
	
}
