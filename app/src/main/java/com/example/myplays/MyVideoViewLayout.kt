package com.example.myplays

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.CountDownTimer
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.TimeUnit

class MyVideoViewLayout : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var videoView: FullVideoView
    private val ivPlay: ImageView = ImageView(context)
    private var tvTime: TextView = TextView(context)
    private var remainTime: Long = 0
    private lateinit var timer: CountDownTimer


    init {
        //Play Button
        ivPlay.apply {
            setImageDrawable(context.getDrawable(R.drawable.ic_play_24dp))
            scaleX = 0.33f
            scaleY = 0.33f
            elevation = 5f
        }
        tvTime.apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            params.setMargins(10, 10, 10, 10)
            setPadding(10, 2, 10, 2)
            gravity = Gravity.CENTER
            params.gravity = Gravity.START or Gravity.BOTTOM
            setBackgroundColor(Color.BLACK)
            layoutParams = params
            elevation = 5f
            background = ContextCompat.getDrawable(context, R.drawable.shader_timer_shape)
            setTextColor(Color.WHITE)
        }
        tvTime.text = getTimeText(0)
        
        



        videoView = FullVideoView(context).apply {

            //Set this setting before video play setting!!
            setOnPreparedListener { p0 ->
                p0?.setVolume(0f, 0f)
                remainTime = duration.toLong()

            }

            setOnClickListener {
                if (this.isPlaying) {
                    ivPlay.visibility = View.VISIBLE
                    cancelTimer()
                    this.pause()
                } else {
                    ivPlay.visibility = View.INVISIBLE
                    startTimer()
                    start()
                }
            }

        }
        addView(tvTime)
        addView(ivPlay)
        addView(videoView)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(remainTime, 1000) {
            override fun onTick(p0: Long) {
                remainTime = p0
                tvTime.text = getTimeText(videoView.duration - remainTime)
            }

            override fun onFinish() {
                remainTime = videoView.duration.toLong()
            }
        }
        timer.start()
    }

    private fun cancelTimer() {
        timer.cancel()
    }

    fun setVideoData(uri: Uri) {
        videoView.setVideoURI(uri)
        videoView.start()
        videoView.pause()
        videoView.seekTo( 1000)
        videoView.setMediaController(null)
    }

    inner class FullVideoView : VideoView {
        constructor(context: Context?) : super(context)
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            val width = View.getDefaultSize(0, widthMeasureSpec)
            val height = View.getDefaultSize(0, heightMeasureSpec)
            setMeasuredDimension(width, height)
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getTimeText(sec: Long): String {
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(sec),
            TimeUnit.MILLISECONDS.toMinutes(sec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sec)),
            TimeUnit.MILLISECONDS.toSeconds(sec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sec))
        )
    }
}

