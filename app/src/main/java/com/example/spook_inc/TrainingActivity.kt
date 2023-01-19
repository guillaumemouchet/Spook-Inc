package com.example.spook_inc

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

import android.widget.Toast

class TrainingActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        val btnAddTeam = findViewById<Button>(R.id.btnAdd)
        val btnGhost = findViewById<ImageButton>(R.id.image6)
        val txtName = findViewById<TextView>(R.id.txtVName)
        val txtStrength = findViewById<TextView>(R.id.txtVStrength)

        val ghost =  Ghost(1,"Charlie", 10)

        txtName.text = "Name: ${ghost.name}"
        txtStrength.text = "Strength: ${ghost.strength}"

        btnGhost.setOnClickListener(){
            btnAddTeam.visibility = View.VISIBLE
            ghost.main()
            btnAddTeam.text = "Add ${ghost.name}"
            var ghostnoise = MediaPlayer.create(this, R.raw.ghost)
            ghostnoise.setVolume(1f, 1f)
            ghostnoise.start()
        }

        btnAddTeam.setOnClickListener(){
            Toast.makeText(this, "AddTeam: ${ghost.name}", Toast.LENGTH_SHORT).show()

        }

    }
    //Cycle de vie d'une application

    override fun onStart() {
        //Start sound of BG

        startService(Intent(this, BackgroundSoundService::class.java))

        // "super" after (continues flow)
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
        //stopService(Intent(this, BackgroundSoundService::class.java))

        // "super" after (continues flow)
        super.onDestroy()
    }

}
