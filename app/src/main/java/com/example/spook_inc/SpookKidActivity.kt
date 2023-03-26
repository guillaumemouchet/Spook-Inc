package com.example.spook_inc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class SpookKidActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0

    private lateinit var viewKid: ImageView
    private lateinit var textKid: TextView

    //Change to local storage team or global Var
    private var playerTeam: List<Ghost> = mutableListOf()//Ghost(1,"Charlie", 10,Ghost_Type.TOPHAT),Ghost(2,"Damien", 100,Ghost_Type.MINITOPHAT))
    private var playerTeamImages: List<ImageView> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spook_kid)
        val g1Image : ImageView = findViewById(R.id.ghostImage1)
        val g2Image : ImageView = findViewById(R.id.ghostImage2)
        val g3Image : ImageView = findViewById(R.id.ghostImage3)

        playerTeamImages = playerTeamImages.plus(g1Image)
        playerTeamImages = playerTeamImages.plus(g2Image)
        playerTeamImages = playerTeamImages.plus(g3Image)

        val context = applicationContext
        val directory = context.filesDir

        //Get player Team
        val filename = "my_team.json"
        val file = File(directory, filename)
        val storedGhostString = file.inputStream().bufferedReader().use { it.readLines() }
        Log.d("Json", storedGhostString.toString())
        val storedGhosts = Json.decodeFromString<List<Ghost>>(storedGhostString.toString())
        playerTeam = playerTeam.plus(storedGhosts)

        viewKid = findViewById(R.id.kidImage)
        textKid = findViewById(R.id.kidNameText)

        val kidStrength: Int = intent.getIntExtra("kidStrength", 0)
        var ghostImage = R.drawable.ghost_normal_back
        var totalStrength = 0
        for (i in playerTeam.indices) {

            totalStrength += playerTeam[i].strength
            when (playerTeam[i].ghostType) {
                Ghost_Type.TOPHAT -> {
                    ghostImage = R.drawable.ghost_tophat_back
                }
                Ghost_Type.MINITOPHAT -> {
                    ghostImage = R.drawable.ghost_minitophat_back
                }
                Ghost_Type.NORMAL -> {
                    ghostImage = R.drawable.ghost_normal_back
                }
                Ghost_Type.SCYTHE -> {
                    ghostImage = R.drawable.ghost_scythe_back
                }
                else -> { // Note the block
                    ghostImage = R.drawable.ghost_normal_back
                }
            }
            playerTeamImages[i].setImageResource(ghostImage)
        }
        print(totalStrength)

        when (kidStrength) {
            100 -> {
                viewKid.setImageResource(R.drawable.karate_kid_photo)
                textKid.text = "The Karate Kids 100"
            }
            300 -> {
                viewKid.setImageResource(R.drawable.nerds)
                textKid.text = "The nerds 300"
            }
            500 -> {
                viewKid.setImageResource(R.drawable.cool_kid)
                textKid.text = "The Cool Kid 500"
            }
            else -> { // Note the block
                print("ERROR")
            }
        }

        if(totalStrength>kidStrength)
        {
            Toast.makeText(applicationContext,"Victory ! \nFinish him",Toast.LENGTH_SHORT).show()
        }
    }

        //Cycle de vie d'une application

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
        //stopService(Intent(this, BackgroundSoundService::class.java))

        // "super" after (continues flow)
        super.onDestroy()
    }
}
