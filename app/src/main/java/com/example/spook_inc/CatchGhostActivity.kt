package com.example.spook_inc

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

class CatchGhostActivity : AppCompatActivity() {
    private lateinit var mainLayout: ViewGroup
    private lateinit var image: ImageView
    // default position of image
    private var xDelta = 0
    private var yDelta = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catch_ghost)
        image = findViewById(R.id.imageView)
        mainLayout = findViewById(R.id.main)
        // Get battery Level
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = this.registerReceiver(null, ifilter)
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level / scale.toFloat() * 100;


        //change the size of image from the battery  * batteryPct/100
        val size = (800 * batteryPct/100).toInt()
        image.getLayoutParams().height = size
        image.getLayoutParams().width = size

        // returns True if the listener has
        // consumed the event, otherwise False.
        image.setOnTouchListener(onTouchListener())


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { view, event ->
            // position information
            // about the event by the user
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            // detecting user actions on moving
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val lParams = view.layoutParams as RelativeLayout.LayoutParams
                    xDelta = x - lParams.leftMargin
                    yDelta = y - lParams.topMargin
                }

                MotionEvent.ACTION_MOVE -> {
                    // based on x and y coordinates (when moving image)
                    // and image is placed with it.
                    val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.leftMargin = x - xDelta
                    layoutParams.topMargin = y - yDelta
                    //layoutParams.rightMargin = 0
                    //layoutParams.bottomMargin = 0
                    view.layoutParams = layoutParams
                }
            }
            // reflect the changes on screen
            mainLayout.invalidate()
            true
        }
    }
}