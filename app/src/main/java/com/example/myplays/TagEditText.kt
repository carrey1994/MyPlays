package com.example.myplays

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class TagEditText : AppCompatEditText {
	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
	
	private var lastText = ""
	private val mHandler = Handler()
	
	override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
		try {
			
			mHandler.removeCallbacksAndMessages(null)
		} catch (e: java.lang.Exception) {
		}
		
		if (text.isNullOrBlank().not() && lastText != text.toString()) {
			try {
				mHandler.postDelayed({
					
					separateText(text.toString())
					val spannableStringBuilder = SpannableStringBuilder(text.toString())
					for (i in 0 until tags.size step 1) {
						if (i % 2 == 0 && i + 1 < tags.size) {
							val colorSpan = ForegroundColorSpan(Color.BLUE)
							spannableStringBuilder.setSpan(colorSpan, tags[i], tags[i + 1], SPAN_INCLUSIVE_INCLUSIVE)
						}
//                        if (i % 2 == 1 && i + 1 < tags.size) {
//                            val colorSpan = ForegroundColorSpan(Color.BLUE)
//                            spannableStringBuilder.setSpan(colorSpan, tags[i], text!!.length, SPAN_INCLUSIVE_INCLUSIVE)
//                        }
					}
					lastText = text.toString()
					val cursorPosition = selectionStart
					setText(spannableStringBuilder)
					this.setSelection(cursorPosition)
					
				}, 1500)
				
			} catch (e: Exception) {
			
			}
		}
		super.onTextChanged(text, start, lengthBefore, lengthAfter)
	}
	
	private val tags = arrayListOf<Int>()
	private fun separateText(text: String) {
		tags.clear()
		for (i in text.indices) {
			if (text[i] == '#' && tags.size % 2 == 0) {
				tags.add(i)
			}
			if (text[i] == ' ' && tags.size % 2 == 1) {
				tags.add(i)
			}
		}
	}
	
	
}