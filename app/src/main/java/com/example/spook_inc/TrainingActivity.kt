package com.example.spook_inc

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter


class TrainingActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0
    var btnAddTeam: Button? = null
    var btnRemoveTeam: Button? = null
    var strengthText: TextView? = null
    var totalSpook: Int = 0

    private var currentGhost : Ghost? = null

    private var playerTeam: List<Ghost> = mutableListOf()

    var playerGhosts: List<Ghost> = mutableListOf()//Ghost(1,"Charlie", 10,Ghost_Type.MINITOPHAT),Ghost(2,"Damien", 100, Ghost_Type.TOPHAT))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        btnAddTeam = findViewById<Button>(R.id.btnAdd)
        btnRemoveTeam = findViewById<Button>(R.id.btnRemove)
        strengthText = findViewById<TextView>(R.id.spookRatingValueText)

        openFiles()
        displayAll()
        btnAddTeam?.setOnClickListener(){
            createActionListenerAddTeam()
        }
        btnRemoveTeam?.setOnClickListener(){
            createActionListenerRemoveTeam()
        }

    }

    private fun displayAll()
    {
        //Display everything
        for (ghost in playerGhosts!!)
        {
            displayGhostPlayer(ghost, this)
        }
        totalSpook = 0

        for (ghost in playerTeam!!)
        {
            displayGhostTeam(ghost, this)
            totalSpook +=ghost.strength
        }
        strengthText?.text = totalSpook.toString()

    }
    private fun openFiles()
    {
        // Get the JSON from the Collection
        //Get the ghosts
        val context = applicationContext
        val directory = context.filesDir

        val filename = "my_ghosts.json"
        val file = File(directory, filename)
        val storedGhostString = file.inputStream().bufferedReader().use { it.readLines() }
        Log.d("Json", storedGhostString.toString())
        val storedGhosts = Json.decodeFromString<List<Ghost>>(storedGhostString.toString())
        playerGhosts = playerGhosts.plus(storedGhosts)

        // Get the JSON from the Team
        try {
            val filenameTeam = "my_team.json"
            val fileTeam = File(directory, filenameTeam)
            val storedGhostStringTeam =
                fileTeam.inputStream().bufferedReader().use { it.readLines() }
            Log.d("Json", storedGhostStringTeam.toString())
            val storedGhostsTeam =
                Json.decodeFromString<List<Ghost>>(storedGhostStringTeam.toString())
            playerTeam = playerTeam.plus(storedGhostsTeam)
        }catch(e: NoSuchFileException)
        {
            //my_team ghost is empty
        }
    }
    private fun displayGhostPlayer(ghost : Ghost, context:Context)
    {
        val ghostLayout = findViewById<LinearLayout>(R.id.ghostLayout)
        val layoutHorizontal = LinearLayout(context)
        val layoutVertical = LinearLayout(context)

        layoutVertical.orientation = LinearLayout.VERTICAL
        layoutHorizontal.orientation = LinearLayout.HORIZONTAL

        val btnAddTeam = findViewById<Button>(R.id.btnAdd)

        val imgBtn = ImageButton(context)
        var ghostImg = R.drawable.ghost

        when (ghost.ghostType) {
            Ghost_Type.TOPHAT -> {
                ghostImg = R.drawable.ghost_tophat_front
            }
            Ghost_Type.MINITOPHAT -> {
                ghostImg = R.drawable.ghost_minitophat_front
            }
            Ghost_Type.NORMAL -> {
                ghostImg = R.drawable.ghost_normal_front
            }
            Ghost_Type.SCYTHE -> {
                ghostImg = R.drawable.ghost_scythe_front
            }
            else -> { // Note the block
                ghostImg = R.drawable.ghost_normal_front
            }
        }
        imgBtn.setImageResource(ghostImg)
        imgBtn?.setOnClickListener(){
            btnAddTeam?.visibility = LinearLayout.VISIBLE
            btnRemoveTeam?.visibility = LinearLayout.INVISIBLE

            //ghost.main()
            btnAddTeam?.text = "Add ${ghost.name}"
            currentGhost = ghost

            var ghostnoise = MediaPlayer.create(context, R.raw.ghost)
            ghostnoise.setVolume(1f, 1f)
            ghostnoise.start()
        }


        val name = TextView(context)
        name.setTextColor(Color.WHITE)
        name.text = "Name: ${ghost.name}"
        layoutVertical.addView(name)

        val strength = TextView(context)
        strength.setTextColor(Color.WHITE)

        strength.text = "Strength: ${ghost.strength}"
        layoutVertical.addView(strength)

        layoutHorizontal.addView(imgBtn)
        layoutHorizontal.addView(layoutVertical)

        ghostLayout.addView(layoutHorizontal)
    }

    private fun displayGhostTeam(ghost : Ghost, context:Context) {
        val ghostTeamLayout = findViewById<LinearLayout>(R.id.ghostTeamLayout)
        val layoutVertical = LinearLayout(context)

        layoutVertical.orientation = LinearLayout.VERTICAL

        val imgBtn = ImageButton(context)
        var ghostImg = R.drawable.ghost_normal_front

        when (ghost.ghostType) {
            Ghost_Type.TOPHAT -> {
                ghostImg = R.drawable.ghost_tophat_front
            }
            Ghost_Type.MINITOPHAT -> {
                ghostImg = R.drawable.ghost_minitophat_front
            }
            Ghost_Type.NORMAL -> {
                ghostImg = R.drawable.ghost_normal_front
            }
            Ghost_Type.SCYTHE -> {
                ghostImg = R.drawable.ghost_scythe_front
            }
            else -> { // Note the block
                ghostImg = R.drawable.ghost_normal_front
            }
        }
        imgBtn.setImageResource(ghostImg)
        imgBtn.setOnClickListener(){
            btnRemoveTeam?.visibility = LinearLayout.VISIBLE
            btnAddTeam?.visibility = LinearLayout.INVISIBLE
            //ghost.main()
            btnRemoveTeam?.text = "Remove ${ghost.name}"
            currentGhost = ghost


            val ghostnoise = MediaPlayer.create(context, R.raw.ghost)
            ghostnoise.setVolume(1f, 1f)
            ghostnoise.start()
        }

        layoutVertical.addView(imgBtn)
        val name = TextView(context)
        name.setTextColor(Color.WHITE)
        name.text = "Name: ${ghost.name}"
        layoutVertical.addView(name)
        ghostTeamLayout.addView(layoutVertical)

    }
    private fun createActionListenerAddTeam()
    {
        if (playerTeam.size >= 3)
        {
            Toast.makeText(this, "Team is full", Toast.LENGTH_SHORT).show()

        }else if(playerTeam.contains(currentGhost))
        {
            Toast.makeText(this, "${ currentGhost?.name} Already in your Team", Toast.LENGTH_SHORT).show()
        }else
        {
            playerTeam = playerTeam.plus(currentGhost!!)

        }
        Toast.makeText(this, "${playerTeam.size}", Toast.LENGTH_SHORT).show()

        //Empty the layout
        val ghostTeamLayout = findViewById<LinearLayout>(R.id.ghostTeamLayout)
        val childCount = ghostTeamLayout.childCount

        for (i in 1..childCount)
        {
            ghostTeamLayout.removeViewAt(0)
        }
        totalSpook =0
        //Put all the ghosts back
        for (ghost in playerTeam)
        {
            displayGhostTeam(ghost,this)
            totalSpook +=ghost.strength
        }
        strengthText?.text = totalSpook.toString()

    }

    private fun createActionListenerRemoveTeam()
    {
        if(playerTeam.contains(currentGhost))
        {
            playerTeam = playerTeam.minus(currentGhost!!)

        }else
        {
            Toast.makeText(this, "${ currentGhost?.name} Not in your Team", Toast.LENGTH_SHORT).show()
        }
        //Empty the layout
        val ghostTeamLayout = findViewById<LinearLayout>(R.id.ghostTeamLayout)
        val childCount = ghostTeamLayout.childCount

        for (i in 1..childCount)
        {
            ghostTeamLayout.removeViewAt(0)
        }
        totalSpook = 0
        //Put all the ghosts back
        for (ghost in playerTeam)
        {
            displayGhostTeam(ghost,this)
            totalSpook+=ghost.strength
        }
        strengthText?.text = totalSpook.toString()

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
        val context = applicationContext
        val directory = context.filesDir
        //save the ghost team
        val filenameTeam = "my_team.json"
        val fileTeam = File(directory, filenameTeam)
        if(fileTeam.exists())
        {
            fileTeam.delete()
            fileTeam.createNewFile()
        }
        for(ghost in playerTeam) {
            var ghostJson = Json.encodeToString(ghost)
            //Since the file is delete no need to check if there are values before
            PrintWriter(FileWriter(fileTeam.path, true)).use {
                if (playerTeam.indexOf(ghost) == playerTeam.size - 1) {
                    it.write(ghostJson)

                } else {
                    it.write(ghostJson)
                    it.write(",")
                }

            }
        }

        //NOT NEEDED HERE save the ghost collection
        //Seul soucis c'est qu'on augmente la taille à chaque fois
        /*val filename = "my_ghosts.json"
        val file = File(directory, filename)
        for(ghost in playerGhosts)
        {
            var ghostJson = Json.encodeToString(ghost)
            Toast.makeText(this, "${playerGhosts.indexOf(ghost)}", Toast.LENGTH_SHORT).show()

            PrintWriter(FileWriter(file.path, true)).use {
                if(playerGhosts.indexOf(ghost)==0) {
                    it.write(",")
                    it.write(ghostJson)
                    it.write(",")

                    Toast.makeText(this, "First", Toast.LENGTH_SHORT).show()

                }
                else if(playerGhosts.indexOf(ghost)==playerGhosts.size-1)
                    {
                        it.write(ghostJson)
                        Toast.makeText(this, "Last", Toast.LENGTH_SHORT).show()

                    }else
                    {
                        it.write(ghostJson)
                        it.write(",")
                        Toast.makeText(this, "Other", Toast.LENGTH_SHORT).show()
                    }

            }
        }*/
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
