package com.example.spook_inc.activitiy

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.spook_inc.tools.BackgroundSoundService
import com.example.spook_inc.R
import java.io.File


class MainActivity : AppCompatActivity() {
    private val COUNTER_KEY = "counter"
    private var counter = 0
    private var lightOff = true

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get from layout
        val btnGhost = findViewById<ImageButton>(R.id.imgBtnGhost)
        val flashLightOff = findViewById<ImageButton>(R.id.imgBtnCatchGhostsOff)
        val btnTraining = findViewById<Button>(R.id.btnTraining)
        val btnSpook = findViewById<Button>(R.id.btnSpook)
        var imgForeground = findViewById<ImageView>(R.id.imgForeground)

        /*
         * The animator makes an object move in the activity
         * The ghost change size to make it more clickable
         */
        val ghostAnimator = ObjectAnimator.ofFloat(btnGhost, "scaleY", 1f, 1.1f, 1f)
        ghostAnimator.duration = 800
        ghostAnimator.repeatCount = ValueAnimator.INFINITE
        ghostAnimator.repeatMode = ObjectAnimator.REVERSE
        ghostAnimator.start()

        /*
         * Creating local storage
         */
        val context = applicationContext
        val directory = context.filesDir

        // Create Collection
        val filename = "my_ghosts.json"
        val ghostFile = File(directory, filename)
        if (!ghostFile.exists()) {
            ghostFile.createNewFile()
        }
        /* DEBUG
        else{
            ghostFile.delete()
            ghostFile.createNewFile()

        }*/

        // Create Team
        val filenameTeam = "my_team.json"
        val ghostFileTeam = File(directory, filenameTeam)
        if (!ghostFileTeam.exists()) {
            ghostFileTeam.createNewFile()

        }
            /* DEBUG
        else
        {
           ghostFileTeam.delete()
           ghostFileTeam.createNewFile()
        }*/


    /*
    * The Flashlight hides some elements when turned off
    * The player must click on it to illuminate the activity and see the buttons
    * A click sound is made on each button press
    */
    flashLightOff.setOnClickListener() {

       var lightclick = MediaPlayer.create(this, R.raw.flashlight);
       lightclick.setVolume(1f, 1f)
       lightclick.start();

       if (lightOff) {

           flashLightOff.setImageResource(R.drawable.flashlight_on);
           btnGhost.isVisible = true;
           btnTraining.isVisible = true;
           btnSpook.isVisible = true;
           imgForeground.alpha = 0.0f;
           lightOff = false;

       } else {

           flashLightOff.setImageResource(R.drawable.flashlight_off);
           btnGhost.isVisible = false;
           btnTraining.isVisible = false;
           btnSpook.isVisible = false;
           imgForeground.alpha = 0.8f;
           lightOff = true;
       }

}

/*
* Add the Listener on the different Buttons and put music on them
* The set Volume doesn't seems to change much
*/

btnGhost.setOnClickListener() {
   var ghostNoise = MediaPlayer.create(this, R.raw.ghost);
    ghostNoise.setVolume(0.9f, 0.9f)
    ghostNoise.start();
   catchGhosts();
}

btnTraining.setOnClickListener() {
   var whistleNoise = MediaPlayer.create(this, R.raw.whistle);
    whistleNoise.setVolume(0.05f, 0.05f)
    whistleNoise.start();
   training();
}

btnSpook.setOnClickListener() {
   var kidNoise = MediaPlayer.create(this, R.raw.cri2);
    kidNoise.setVolume(0.08f, 0.08f)
    kidNoise.start();
   spook();
}

}

/*
* Open the different Activities
*/

private fun catchGhosts() {
val intent = Intent(this, CameraActivity::class.java);
startActivity(intent);
}

private fun training() {
val intent = Intent(this, TrainingActivity::class.java);
startActivity(intent);
}

private fun spook() {
val intent = Intent(this, SpookActivity::class.java);
startActivity(intent);
}


//Life Cycle of the application

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
stopService(Intent(this, BackgroundSoundService::class.java))

// "super" after (continues flow)
super.onDestroy()
}
}