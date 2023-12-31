package com.example.spook_inc.activitiy

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.spook_inc.tools.BackgroundSoundService
import com.example.spook_inc.tools.Ghost
import com.example.spook_inc.tools.GhostType
import com.example.spook_inc.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File


class SpookKidActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0

    private lateinit var viewKid: ImageView
    private lateinit var textKid: TextView
    private var totalStrength = 0
    //Change to local storage team or global Var
    private var playerTeam: List<Ghost> = mutableListOf()// DEBUG GHOSTS : Ghost(1,"Charlie", 10,GhostType.TOPHAT),Ghost(2,"Damien", 100,GhostType.MINITOPHAT))
    private var playerTeamImages: List<ImageView> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spook_kid)
        //Get values from layout
        val g1Image : ImageView = findViewById(R.id.ghostImage1)
        val g2Image : ImageView = findViewById(R.id.ghostImage2)
        val g3Image : ImageView = findViewById(R.id.ghostImage3)

        viewKid = findViewById(R.id.kidImage)
        textKid = findViewById(R.id.kidNameText)

        /*
         * Used to change dynamically the image of our team
         */
        val spookMeterLabel : TextView = findViewById(R.id.spookometer_text)
        val spookMeterBar : ProgressBar = findViewById(R.id.spookOmeterProgress)

        playerTeamImages = playerTeamImages.plus(g1Image)
        playerTeamImages = playerTeamImages.plus(g2Image)
        playerTeamImages = playerTeamImages.plus(g3Image)


        // Get context
        val context = applicationContext
        val directory = context.filesDir

        // Get player Team
        val filename = "my_team.json"
        val file = File(directory, filename)
        val storedGhostString = file.inputStream().bufferedReader().use { it.readLines() }
        Log.d("Json", storedGhostString.toString())
        val storedGhosts = Json.decodeFromString<List<Ghost>>(storedGhostString.toString())
        playerTeam = playerTeam.plus(storedGhosts)



        /*
         * Get the ghosts in our teams and set the corresponding image to the playerTeamImages
         * Get the total strength to see if we win against the kids
         */
        var ghostImage = R.drawable.ghost_normal_back
        for (i in playerTeam.indices) {
            totalStrength += playerTeam[i].strength
            playerTeamImages[i].setImageResource(playerTeam[i].getImageBack())
        }

        /*
         * Get the kid Strength from the previous activity
         * It will decide what kids they will fight
         */
        val kidStrength: Int = intent.getIntExtra("kidStrength", 100)
        when (kidStrength) {
            100 -> {
                viewKid.setImageResource(R.drawable.karate_kid_photo)
                textKid.text = "The Karate Kids"
            }
            300 -> {
                viewKid.setImageResource(R.drawable.nerds)
                textKid.text = "The nerds"
            }
            500 -> {
                viewKid.setImageResource(R.drawable.cool_kid)
                textKid.text = "The Cool Kid"
            }
            else -> { // Note the block
                print("ERROR")
            }
        }

        spookMeterLabel.text = buildString {
            append("Spook O Meter ")
            append(totalStrength)
        }
        // Create ObjectAnimator to animate the ProgressBar's progress
        val percentLeft = 100 - totalStrength*100/kidStrength
        val animator = ObjectAnimator.ofInt(spookMeterBar, "progress", 100, if (percentLeft < 0 ) 0 else percentLeft)

        // Set duration of the animation (in milliseconds)
        animator.duration = 5000

        // Start the animation
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                //no use here
            }

            override fun onAnimationEnd(p0: Animator?) {
                if(totalStrength>kidStrength)
                {
                    Toast.makeText(applicationContext,"Victory ! \nFinish him",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(applicationContext,"You lose! \nTry to be spookier next time",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAnimationCancel(p0: Animator?) {
                //no use here

            }

            override fun onAnimationRepeat(p0: Animator?) {
                //no use here

            }
        })
        animator.start()
    }

        //Life Cycle of an application

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
