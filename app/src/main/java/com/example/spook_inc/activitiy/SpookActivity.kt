package com.example.spook_inc.activitiy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.spook_inc.tools.BackgroundSoundService
import com.example.spook_inc.tools.Ghost
import com.example.spook_inc.my_widgets.HouseButton
import com.example.spook_inc.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class SpookActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0

    private lateinit var btnHouse3: HouseButton
    private lateinit var btnHouse2: HouseButton
    private lateinit var btnHouse1: HouseButton
    private var storedGhosts: List<Ghost> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spook)
        btnHouse3 = findViewById(R.id.house_difficulty_3)
        btnHouse2 = findViewById(R.id.house_difficulty_2)
        btnHouse1 = findViewById(R.id.house_difficulty_1)

        val context = applicationContext
        val directory = context.filesDir

        // Get player Team
        val filename = "my_team.json"
        val file = File(directory, filename)
        val storedGhostString = file.inputStream().bufferedReader().use { it.readLines() }
        Log.d("Json", storedGhostString.toString())
        storedGhosts = Json.decodeFromString<List<Ghost>>(storedGhostString.toString())

            btnHouse3.setOnClickListener {
                startSpook(500)
            }
            btnHouse2.setOnClickListener {
                startSpook(300)
            }
            btnHouse1.setOnClickListener {
                startSpook(100)
            }


    }

    private fun startSpook(kidStrength: Int)
    {
        if(storedGhosts.size!=0) {
        val intent = Intent(this, SpookKidActivity::class.java)
        intent.putExtra("kidStrength", kidStrength)

        startActivity(intent)
        }else
        {
            Toast.makeText(applicationContext,"You have no one in your team",Toast.LENGTH_SHORT).show()
        }
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