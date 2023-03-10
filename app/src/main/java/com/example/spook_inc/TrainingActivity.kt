package com.example.spook_inc

import android.content.Context
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

import android.widget.*


class TrainingActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0
    var btnAddTeam: Button? = null
    var btnGhost : ImageButton? = null
    private var currentGhost : Ghost? = null
    var playerTeamText: TextView? = null

    private var playerTeam: List<Ghost> = mutableListOf()

    var playerGhosts: List<Ghost> = mutableListOf(Ghost(1,"Charlie", 10),Ghost(2,"Damien", 100))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        btnAddTeam = findViewById<Button>(R.id.btnAdd)
        playerTeamText = findViewById<TextView>(R.id.playerTeamText)

        playerGhosts = playerGhosts.plus(Ghost(3,"Valentin", 30))
        for (ghost in playerGhosts!!)
        {
            displayGhost(ghost, this)
        }


        btnAddTeam?.setOnClickListener(){
            createActionListenerTeam()
        }

    }

    private fun displayGhost(ghost : Ghost, context:Context)
    {

        val ghostLayout = findViewById<LinearLayout>(R.id.ghostLayout)
        val layoutHorizontal = LinearLayout(context)
        val layoutVertical = LinearLayout(context)

        layoutVertical.orientation = LinearLayout.VERTICAL
        layoutHorizontal.orientation = LinearLayout.HORIZONTAL

        val btnAddTeam = findViewById<Button>(R.id.btnAdd)

        val imgBtn = ImageButton(context)
        imgBtn.setImageResource(R.drawable.ghost)

        imgBtn?.setOnClickListener(){
            btnAddTeam?.visibility = LinearLayout.VISIBLE
            ghost.main()
            btnAddTeam?.text = "Add ${ghost.name}"
            currentGhost = ghost


            var ghostnoise = MediaPlayer.create(context, R.raw.ghost)
            ghostnoise.setVolume(0.5f, 0.5f)
            ghostnoise.start()
        }


        val name = TextView(context)
        name.text = "Name: ${ghost.name}"
        layoutVertical.addView(name)

        val strength = TextView(context)
        strength.text = "Strength: ${ghost.strength}"
        layoutVertical.addView(strength)

        layoutHorizontal.addView(imgBtn)
        layoutHorizontal.addView(layoutVertical)

        ghostLayout.addView(layoutHorizontal)
    }
    private fun createActionListenerTeam()
    {
        val totalText = findViewById<TextView>(R.id.spookRatingValueText)


        if(playerTeam.contains(currentGhost))
        {
            Toast.makeText(this, "${ currentGhost?.name} Already in your Team", Toast.LENGTH_SHORT).show()
        }else
        {
            playerTeam = playerTeam.plus(currentGhost!!)

        }

        playerTeamText?.text = ""
        var text : String = ""
        var total : Int = 0
        for (ghost in playerTeam!!)
        {
            text = text.plus(ghost.name).plus("\t")
            total += ghost.strength
        }

        playerTeamText?.text = text
        totalText.text =total.toString()

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
