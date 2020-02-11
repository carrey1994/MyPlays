package com.example.myplays.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplays.databinding.ItemRadioDataBinding

class RadioAdapter(private var radioData: List<RadioData>) : RecyclerView.Adapter<RadioAdapter.RadioDataViewHolder>() {
	
	private var lastIndex: Int = -1
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioDataViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = ItemRadioDataBinding.inflate(inflater, parent, false)
		return RadioDataViewHolder(binding)
	}
	
	override fun getItemCount(): Int = radioData.size
	
	override fun onBindViewHolder(holder: RadioDataViewHolder, position: Int) {
		holder.bind(radioData[position])
		holder.itemView.setOnClickListener {
			if (lastIndex == position) return@setOnClickListener
			radioData[position].isCheck = !radioData[position].isCheck
			notifyItemChanged(position)
			if (lastIndex == -1) {
				lastIndex = position
			} else {
				radioData[lastIndex].isCheck = !radioData[lastIndex].isCheck
				notifyItemChanged(lastIndex)
				lastIndex = position
			}
		}
		
	}
	
	inner class RadioDataViewHolder(private val binding: ItemRadioDataBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(radioData: RadioData) {
			binding.radioData = radioData
			binding.executePendingBindings()
		}
	}
	
	
}