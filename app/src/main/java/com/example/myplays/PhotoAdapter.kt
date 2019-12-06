package com.example.myplays

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PhotoAdapter(private val photoModels: List<PhotoModel>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var pickListener: OnPickListener? = null
    private var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, null, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int = photoModels.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.ivPhoto.setImageBitmap(photoModels[position].bitmap)
        holder.itemView.isSelected = photoModels[position].isCheck
        val fnChange = {
            photoModels[position].isCheck = holder.itemView.isSelected
            holder.ivCheck.isSelected = holder.itemView.isSelected
            notifyItemChanged(position)
            pickListener?.onPick(photoModels[position])
        }
        holder.itemView.setOnClickListener {
            if (count == 4 && !it.isSelected) return@setOnClickListener
            if (count < 4) {
                it.isSelected = !it.isSelected
                if (it.isSelected)
                    count++
                else
                    count--
                fnChange()
            } else if (count == 4) {
                if (it.isSelected) {
                    it.isSelected = !it.isSelected
                    count--
                    fnChange()
                }
            }

        }
    }

    inner class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPhoto = view.findViewById<ImageView>(R.id.iv_photo)
        val ivCheck = view.findViewById<ImageView>(R.id.iv_check)
    }

    fun setOnPickListener(pickListener: OnPickListener) {
        this.pickListener = pickListener
    }

    interface OnPickListener {
        fun onPick(photoModel: PhotoModel)
    }
}