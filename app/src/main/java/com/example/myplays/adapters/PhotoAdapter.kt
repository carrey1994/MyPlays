package com.example.myplays.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplays.PhotoModel
import com.example.myplays.databinding.ItemPhotoBinding

class PhotoAdapter(private val photoModels: List<PhotoModel>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
	
	private var pickListener: OnPickListener? = null
	private var count = 0
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = ItemPhotoBinding.inflate(inflater, parent, false)
		return PhotoViewHolder(binding)
	}
	
	override fun getItemCount(): Int = photoModels.size
	
	override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
		holder.bind(photoModels[position])
		
		holder.itemView.setOnClickListener {
			when (count) {
				4 -> {
					if (photoModels[position].isCheck) {
						photoModels[position].isCheck = !photoModels[position].isCheck
						count--
						notifyItemChanged(position)
						pickListener?.onPick(photoModels[position])
					}
				}
				in 0 until 4 -> {
					photoModels[position].isCheck = !photoModels[position].isCheck
					if (photoModels[position].isCheck)
						count++
					else
						count--
					notifyItemChanged(position)
					pickListener?.onPick(photoModels[position])
				}
			}
		}
	}
	
	
	inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
		RecyclerView.ViewHolder(binding.root) {
		
		fun bind(photoModel: PhotoModel) {
			binding.photo = photoModel
			binding.executePendingBindings()
		}
		
	}
	
	fun setOnPickListener(pickListener: OnPickListener) {
		this.pickListener = pickListener
	}
	
	interface OnPickListener {
		fun onPick(photoModel: PhotoModel)
	}
	
}