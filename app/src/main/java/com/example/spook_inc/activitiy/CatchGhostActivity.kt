package com.example.spook_inc.activitiy

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.graphics.Rect
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import android.widget.*
import com.example.spook_inc.tools.BackgroundSoundService
import com.example.spook_inc.tools.Ghost
import com.example.spook_inc.tools.GhostType
import com.example.spook_inc.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class CatchGhostActivity : AppCompatActivity() {

   // private var ghost = Ghost("John", 123, GhostType.TOPHAT)
    //private var ghost2 = Ghost("Jane", 1000, GhostType.MINITOPHAT)
    private var ghost = Ghost.Companion.createRandomGhost()
    private lateinit var mainLayout: ViewGroup
    private lateinit var image: ImageView
    private lateinit var imgGhost: ImageView

    private var capture_value = 0
    private var captured: Boolean = false
    // default position of image
    private var xDelta = 0
    private var yDelta = 0
    private val COUNTER_KEY = "counter"
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catch_ghost)
        image = findViewById(R.id.imageView)
        imgGhost = findViewById<ImageView>(R.id.imageGhost)
        mainLayout = findViewById(R.id.main)


        // Get battery Level
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = this.registerReceiver(null, filter)
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        var batteryPct = level / scale.toFloat() * 100

        //change the size of image from the battery * batteryPct/100
        val size = (800 * batteryPct/100).toInt()
        image.layoutParams.height = size
        image.layoutParams.width = size

        // returns True if the listener has
        // consumed the event, otherwise False.
        image.setOnTouchListener(onTouchListener())

        var ghostImg = R.drawable.ghost

        when (ghost.ghostType) {
            GhostType.TOPHAT -> {
                ghostImg = R.drawable.ghost_tophat_front
            }
            GhostType.MINITOPHAT -> {
                ghostImg = R.drawable.ghost_minitophat_front
            }
            GhostType.NORMAL -> {
                ghostImg = R.drawable.ghost_normal_front
            }
            GhostType.SCYTHE -> {
                ghostImg = R.drawable.ghost_scythe_front
            }
            }

        imgGhost.setImageResource(ghostImg)
        val path = Path()
        val centerX = -50f // x-coordinate of the center of the ellipse
        val centerY = -100f // y-coordinate of the center of the ellipse
        val radiusX = 300f // horizontal radius of the ellipse
        val radiusY = 800f // vertical radius of the ellipse
        path.addOval(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY, Path.Direction.CW)

        val animator = ObjectAnimator.ofFloat(imgGhost, View.TRANSLATION_X, View.TRANSLATION_Y, path)
        animator.duration = 8000 // Set the duration of the animation to 4 seconds
        animator.repeatCount = ValueAnimator.INFINITE // Set the repeat count to infinite
        animator.interpolator = PathInterpolator(0.5f, 0f) // Apply a circular path interpolator
        animator.start()


    }


    /*
     * Used to move the torch (Yellow circle) in the Activity
     */
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
                    layoutParams.rightMargin = x + 10 * xDelta
                    layoutParams.bottomMargin = y + 10 * yDelta
                    view.layoutParams = layoutParams
                }
            }
            // reflect the changes on screen
            mainLayout.invalidate()
            val rect1 = Rect()
            imgGhost.getHitRect(rect1)

            val rect2 = Rect()
            view.getHitRect(rect2)

            if (Rect.intersects(rect1, rect2) && !captured) {
                // The two views are touching so add to the capture meter
                capture_value += 1
            } else {
                // The two views are not touching
            }


            if(capture_value>=ghost.strength && !captured)
            {
                captured=true
                Toast.makeText(applicationContext,"Well done ! Ghost captured",Toast.LENGTH_SHORT).show()

                val context = applicationContext
                val directory = context.filesDir
                val filename = "my_ghosts.json"
                val file = File(directory, filename)

                /* Get what's already in to know how to serialize */
                val storedGhostString = file.inputStream().bufferedReader().use { it.readLines() }
                val storedGhosts = Json.decodeFromString<List<Ghost>>(storedGhostString.toString())
                Log.d("already in", storedGhosts.toString())


                val ghostJson = Json.encodeToString(ghost)
                PrintWriter(FileWriter(file.path, true)).use {
                    if (storedGhosts.isNotEmpty()) {
                        it.write(",$ghostJson")
                    } else if (storedGhosts.isEmpty()) {
                        it.write("$ghostJson")
                    }
                }

            }
            true
        }
    }

    //Cycle de vie d'une application

    override fun onStart() {
        // "super" after (continues flow)

        startService(Intent(this, BackgroundSoundService::class.java))

        super.onStart()
    }

    override fun onRestart() {
        // "super" after (continues flow)

        super.onRestart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // "super" before (populates our Bundle object)
        super.onRestoreInstanceState(savedInstanceState)

        // Here, the Bundle object is never null
        counter = savedInstanceState.getInt(COUNTER_KEY, 0)
    }

    override fun onResume() {
        // "super" after (continues flow)
        super.onResume()
    }

    override fun onPause() {
        startService(Intent(this, BackgroundSoundService::class.java))

        super.onPause()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        ++counter
        savedInstanceState.putInt(COUNTER_KEY, counter)

        // "super" after (saves our Bundle object, continues flow)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onStop() {
        // "super" after (continues flow)

        super.onStop()
    }

    override fun onDestroy() {

        // "super" after (continues flow)
        super.onDestroy()
    }
}