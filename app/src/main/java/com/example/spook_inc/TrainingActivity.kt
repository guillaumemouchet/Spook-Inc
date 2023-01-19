package com.example.spook_inc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton

import android.widget.Toast

class TrainingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        val btnAddTeam = findViewById<Button>(R.id.btnAdd)
        val btnGhost = findViewById<ImageButton>(R.id.image6)

        val ghost =  Ghost(1,"Charlie", 10)

        btnGhost.setOnClickListener(){
            btnAddTeam.visibility = View.VISIBLE
            ghost.main()
            btnAddTeam.text = "Add ${ghost.name}"
        }

        btnAddTeam.setOnClickListener(){
            Toast.makeText(this, "AddTeam: ${ghost.name}", Toast.LENGTH_SHORT).show()
            //var ghostnoise = MediaPlayer.create(this, R.raw.ghost)
            //ghostnoise.setVolume(1f, 1f)
            //ghostnoise.start()
        }

    }

}
