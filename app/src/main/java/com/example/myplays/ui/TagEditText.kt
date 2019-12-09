package com.example.myplays.ui

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class TagEditText : AppCompatEditText {
	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
	
	private var lastText = ""
	private val mHandler = Handler()

	init {
		addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(p0: Editable?) {
				//remove behaviors on typing
				mHandler.removeCallbacksAndMessages(null)
				if (text.isNullOrBlank().not() && lastText != text.toString()) {
					mHandler.postDelayed({
						separateText("$text")
						val spannableStringBuilder = SpannableStringBuilder("$text")
						for (i in 0 until tags.size step 1) {
							if (i % 2 == 0 && i + 1 < tags.size) {
								val colorSpan = ForegroundColorSpan(Color.BLUE)
								spannableStringBuilder.setSpan(colorSpan, tags[i], tags[i + 1], SPAN_INCLUSIVE_INCLUSIVE)
							}
						}
						lastText = "$text"
						val cursorPosition = selectionStart
						text = spannableStringBuilder
						this@TagEditText.setSelection(cursorPosition)
					}, 500)
				}
			}
			
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			
			}
			
			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
			}
		})
		
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