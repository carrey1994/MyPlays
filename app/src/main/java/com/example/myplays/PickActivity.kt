package com.example.myplays

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myplays.adapters.PhotoAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.picker_view.*

class PickActivity : AppCompatActivity() {
	
	private val pickViewModel by lazy {
		ViewModelProviders.of(this).get(PickViewModel::class.java)
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.picker_view)
		setupViews()
		observePhotoData()
		pickViewModel.dealData(intent.extras!![SEARCH_DATA] as HashMap<Boolean, List<*>>, contentResolver)
	}
	
	private fun observePhotoData() = pickViewModel.photoLiveData.observe(this, Observer {
		pb_load.visibility = View.GONE
		val adapter = PhotoAdapter(it)
		adapter.setOnPickListener(object : PhotoAdapter.OnPickListener {
			override fun onPick(photoModel: PhotoModel) {
				if (photoModel.isCheck)
					pickViewModel.pickPhotos.add(photoModel.index)
				else
					pickViewModel.pickPhotos.remove(photoModel.index)
			}
		})
		rv_photo.adapter = adapter
	})
	
	private fun setupViews() {
		rv_photo.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
		btn_enter.setOnClickListener {
			val intent = Intent()
			intent.putExtra(IMAGES_ID, pickViewModel.pickPhotos)
			setResult(RESULT_PHOTO_CODE, intent)
			finish()
		}
	}
}