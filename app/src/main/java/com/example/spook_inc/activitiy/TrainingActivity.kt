package com.example.spook_inc.activitiy

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
import kotlin.math.roundToInt


class TrainingActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0
    var btnAddTeam: Button? = null
    var btnRemoveTeam: Button? = null
    var strengthText: TextView? = null
    var totalSpook: Int = 0

    private var currentGhost : Ghost? = null

    private var playerTeam: List<Ghost> = mutableListOf()
    var playerGhosts: List<Ghost> = mutableListOf(
   //DEBUG GHOSTS : // Ghost("Charlie", 10, GhostType.SCYTHE), //    Ghost("Damien", 100, GhostType.TOPHAT)
    )
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


    /*
     * Open the Json files and store them in the MutableLists
     */
    private fun openFiles()
    {
        // Get the JSON from the Collection
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
            val storedGhostStringTeam = fileTeam.inputStream().bufferedReader().use { it.readLines() }
            Log.d("Json", storedGhostStringTeam.toString())
            val storedGhostsTeam = Json.decodeFromString<List<Ghost>>(storedGhostStringTeam.toString())
            playerTeam = playerTeam.plus(storedGhostsTeam)
        }catch(e: NoSuchFileException)
        {
            //my_team ghost is empty
        }
    }

    /*
     * Display all the content in the Ghost collection and the Ghost team
     */
    private fun displayAll()
    {
        //Display everything
        for (ghost in playerGhosts)
        {
            displayGhostPlayer(ghost, this)
        }
        totalSpook = 0

        for (ghost in playerTeam)
        {
            displayGhostTeam(ghost, this)
            totalSpook +=ghost.strength
        }
        strengthText?.text = totalSpook.toString()

    }
    /*
     * Display a Ghost in the ScrollView of the collection
     */
    private fun displayGhostPlayer(ghost : Ghost, context:Context)
    {
        val ghostLayout = findViewById<LinearLayout>(R.id.ghostLayout)
        val layoutHorizontal = LinearLayout(context)
        val layoutVertical = LinearLayout(context)
        val btnAddTeam = findViewById<Button>(R.id.btnAdd)

        layoutVertical.orientation = LinearLayout.VERTICAL
        layoutHorizontal.orientation = LinearLayout.HORIZONTAL

        val imgBtn = ImageButton(context)

        imgBtn.setImageResource(ghost.getImageFront())

        //When you click on the image you see an add button to put it in your team
        imgBtn?.setOnClickListener(){
            btnAddTeam?.visibility = LinearLayout.VISIBLE
            btnRemoveTeam?.visibility = LinearLayout.INVISIBLE
            btnAddTeam?.text = "Add ${ghost.name}"
            currentGhost = ghost

            //Sound of Ghost when you click on it
            var ghostNoise = MediaPlayer.create(context, R.raw.ghost)
            ghostNoise.setVolume(1f, 1f)
            ghostNoise.start()
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

    /*
     * Display a Ghost in the HorizontalScrollView of the team
     */
    private fun displayGhostTeam(ghost : Ghost, context:Context) {
        val ghostTeamLayout = findViewById<LinearLayout>(R.id.ghostTeamLayout)
        val layoutVertical = LinearLayout(context)

        layoutVertical.orientation = LinearLayout.VERTICAL

        // Choose the Image of the Ghost, it depends on his GhostType
        var imgBtn = ImageButton(context)

        //Rescale the image so it fits in the HorizontalScrollView
        //ghostImg is an Int so we need to convert it to an (Bitmap)Image before rescaling it
        val b = BitmapFactory.decodeResource(context.resources, ghost.getImageFront());
        val sizeX = (b.width * 0.90).roundToInt()
        val sizeY = (b.width * 0.90).roundToInt()
        val bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false)

        imgBtn.setImageBitmap(bitmapResized)

        //When you click on the image you see an remove button to remove it from your team
        imgBtn.setOnClickListener(){
            btnRemoveTeam?.visibility = LinearLayout.VISIBLE
            btnAddTeam?.visibility = LinearLayout.INVISIBLE
            btnRemoveTeam?.text = "Remove ${ghost.name}"
            currentGhost = ghost
        }
        layoutVertical.addView(imgBtn)

        val name = TextView(context)
        name.setTextColor(Color.WHITE)
        name.text = "Name: ${ghost.name}"
        layoutVertical.addView(name)
        ghostTeamLayout.addView(layoutVertical)

    }



    /*
     * Add Ghost in Team
     * The maximum is 3
     * Can't have twice the same ghost
     */
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

    /*
     * Remove Ghost in Team
     */
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

    //Life Cycle of the application

    //We don't want to go back on the capture so we go on the mainActivity
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)    }
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
        // Pause the service
        startService(Intent(this, BackgroundSoundService::class.java))

        // Get Context
        val context = applicationContext
        val directory = context.filesDir

        // Save team in the JSON file
        val filenameTeam = "my_team.json"
        val fileTeam = File(directory, filenameTeam)
        // Delete the file to start over
        if(fileTeam.exists())
        {
            fileTeam.delete()
            fileTeam.createNewFile()
        }
        for(ghost in playerTeam) {
            var ghostJson = Json.encodeToString(ghost)
            //Since the file is delete no need to check if there are values before
            PrintWriter(FileWriter(fileTeam.path, true)).use {
                //The last one must not put a "," or else it won't work
                if (playerTeam.indexOf(ghost) == playerTeam.size - 1) {
                    it.write(ghostJson)
                } else {
                    it.write(ghostJson)
                    it.write(",")
                }
            }
        }

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
