package com.example.spook_inc.activitiy

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.graphics.Rect
import android.net.Uri
import android.opengl.Visibility
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

    private var ghost = Ghost.Companion.createRandomGhost()
    private lateinit var mainLayout: ViewGroup
    private lateinit var image: ImageView
    private lateinit var imgGhost: ImageView

    private var captureValue = 0
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
        imgGhost = findViewById(R.id.imageGhost)
        mainLayout = findViewById(R.id.main)

        val imageView: ImageView = findViewById(R.id.imgViewBackground)
        //Use saveUri, instantiate in another class
        var sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        var uriString = sharedPref.getString("uri_key", null)

        //Set the image in the background
        if (uriString != null) {
            val saveUri = Uri.parse(uriString)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.setImageURI(saveUri)
        }

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

        //Change the image of the ghost
        imgGhost.setImageResource(ghost.getImageFront())

        //Define the path of the ghost
        val path = Path()
        val centerX = -50f // x-coordinate of the center of the ellipse
        val centerY = -75f // y-coordinate of the center of the ellipse
        val radiusX = 300f // horizontal radius of the ellipse
        val radiusY = 800f // vertical radius of the ellipse
        path.addOval(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY, Path.Direction.CW)

        //Animate it to make it move following the path
        val animator = ObjectAnimator.ofFloat(imgGhost, View.TRANSLATION_X, View.TRANSLATION_Y, path)
        animator.duration = 8000 // Set the duration of the animation to 4 seconds
        animator.repeatCount = ValueAnimator.INFINITE // Set the repeat count to infinite
        animator.interpolator = PathInterpolator(0.5f, 0f) // Apply a circular path interpolator
        animator.start()

        // returns True if the listener has
        // consumed the event, otherwise False.
        image.setOnTouchListener(onTouchListener())

    }


    /*
     * Used to move the torch (Yellow circle) in the Activity
     * Check if the ghost is getting captured
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchListener(): View.OnTouchListener {
        //Move the Yellow Circle
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

            //Check the collisions of both the yellow circle and the ghost
            val rect1 = Rect()
            imgGhost.getHitRect(rect1)

            val rect2 = Rect()
            view.getHitRect(rect2)

            if (Rect.intersects(rect1, rect2) && !captured) {
                // The two views are touching so add to the capture meter
                captureValue += 1
            }

            //The capture time depends on the strength
            if(captureValue>=ghost.strength && !captured)
            {
                captured=true
                Toast.makeText(applicationContext,"Well done ! Ghost captured",Toast.LENGTH_SHORT).show()

                imgGhost.visibility = View.INVISIBLE
                val context = applicationContext
                val directory = context.filesDir
                val filename = "my_ghosts.json"
                val file = File(directory, filename)


                /* Get what's already in to know how to serialize */
                val storedGhostString = file.inputStream().bufferedReader().use { it.readLines() }
                val storedGhosts = Json.decodeFromString<List<Ghost>>(storedGhostString.toString())

                //save the ghost in the serialized files
                val ghostJson = Json.encodeToString(ghost)
                PrintWriter(FileWriter(file.path, true)).use {
                    if (storedGhosts.isNotEmpty()) {
                        it.write(",$ghostJson")
                    } else if (storedGhosts.isEmpty()) {
                        it.write("$ghostJson")
                    }
                }


                Thread.sleep(1000)

                //Redirect on the Training Activity
                val intent = Intent(this, TrainingActivity::class.java);
                startActivity(intent);

            }
            true
        }
    }

    //Life Cycle of the application
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