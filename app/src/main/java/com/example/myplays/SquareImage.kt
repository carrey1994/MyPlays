package com.example.myplays

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class SquareImage : ImageView {
	
	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
	
	init {
		setPadding(0, 0, 0, 0)
	}
	
	val erasor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
		style = Paint.Style.STROKE
		strokeWidth = 1f
		color = Color.TRANSPARENT
	}
	
	@SuppressLint("DrawAllocation")
	override fun onDraw(canvas: Canvas?) {
		val clipPath = Path()
		val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
		clipPath.addRoundRect(rectF, 15f, 15f, Path.Direction.CW)
		canvas?.clipPath(clipPath)
		super.onDraw(canvas)
	}
	
//	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//		setMeasuredDimension(500, 500)
//	}
}