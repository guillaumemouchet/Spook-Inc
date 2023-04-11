package com.example.spook_inc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SpookActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0

    private lateinit var btnHouse3: HouseButton
    private lateinit var btnHouse2: HouseButton
    private lateinit var btnHouse1: HouseButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spook)
        btnHouse3 = findViewById(R.id.house_difficulty_3)
        btnHouse2 = findViewById(R.id.house_difficulty_2)
        btnHouse1 = findViewById(R.id.house_difficulty_1)

        btnHouse3.setOnClickListener{
            startSpook(500)
        }
        btnHouse2.setOnClickListener{
            startSpook(300)
        }
        btnHouse1.setOnClickListener{
            startSpook(100)
        }

    }

    private fun startSpook(kidStrength: Int)
    {
        val intent = Intent(this, SpookKidActivity::class.java)
        intent.putExtra("kidStrength", kidStrength)

        startActivity(intent)
    }

    //Cycle de vie dune application

    override fun onStart() {
        //Start sound of BG

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