package com.example.spook_inc.activitiy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.spook_inc.tools.BackgroundSoundService
import com.example.spook_inc.tools.Ghost
import com.example.spook_inc.tools.GhostType
import com.example.spook_inc.R
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class CatchGhostActivity : AppCompatActivity() {

    private var ghost = Ghost(1, "John", 123, GhostType.TOPHAT)
    private var ghost2 = Ghost(55, "Jane", 1000, GhostType.MINITOPHAT)

    private lateinit var mainLayout: ViewGroup
    private lateinit var image: ImageView
    // default position of image
    private var xDelta = 0
    private var yDelta = 0
    private val COUNTER_KEY = "counter"
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catch_ghost)
        image = findViewById(R.id.imageView)
        mainLayout = findViewById(R.id.main)
        val btnTest = findViewById<Button>(R.id.btn_test)

        val imageView: ImageView = findViewById(R.id.imgViewBackground)
        //Use saveUri, instantiate in another class
        var sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        var uriString = sharedPref.getString("uri_key", null)

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

        // returns True if the listener has
        // consumed the event, otherwise False.
        image.setOnTouchListener(onTouchListener())

        /*
         * Temporary Elements
         */
        //Can't add twice those ghosts
        // TODO Check if they are already values in the JSON
        // if yes, need to add a "," before adding
        // Else if it's the first only need to add the ghost
        btnTest.setOnClickListener {
            val context = applicationContext
            val directory = context.filesDir

            val filename = "my_ghosts.json"
            val file = File(directory, filename)
            var ghostJson = Json.encodeToString(ghost)
            PrintWriter(FileWriter(file.path, true)).use {
                it.write("$ghostJson,")
            }
            ghostJson = Json.encodeToString(ghost2)
            PrintWriter(FileWriter(file.path, true)).use {
                it.write(ghostJson)
            }
        /*
         * End of Temporary Elements
         */


        /*
         * TODO : Catch the ghost and put it in the JSON
         */
            // TODO Check if they are already values in the JSON
            // if yes, need to add a "," before adding
            // Else if it's the first only need to add the ghost
        }

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
            val testPointer = event.pointerCount.toInt()
            val total = (testPointer < 1);

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
                    layoutParams.rightMargin = x + 10*xDelta
                    layoutParams.bottomMargin = y + 10*yDelta
                    view.layoutParams = layoutParams
                }
            }
            // reflect the changes on screen
            mainLayout.invalidate()
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