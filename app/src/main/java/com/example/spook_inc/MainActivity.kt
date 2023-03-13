package com.example.spook_inc

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import java.io.File


class MainActivity : AppCompatActivity() {
    private val activePlayers: Set<MediaPlayer> = HashSet()
    private val COUNTER_KEY = "counter"
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnGhost = findViewById<ImageButton>(R.id.imgBtnGhost)

        val flashLightOff = findViewById<ImageButton>(R.id.imgBtnCatchGhostsOff)

        val btnTraining = findViewById<Button>(R.id.btnTraining)
        val btnSpook = findViewById<Button>(R.id.btnSpook)

        var imgForeground = findViewById<ImageView>(R.id.imgForeground)

        val ghostAnimator = ObjectAnimator.ofFloat(btnGhost, "scaleY", 1f, 1.1f, 1f)
        ghostAnimator.duration = 800
        ghostAnimator.repeatCount = ValueAnimator.INFINITE
        ghostAnimator.repeatMode = ObjectAnimator.REVERSE

        ghostAnimator.start()

        var lightOff = true

        //Creating local storage
        val context = applicationContext
        val directory = context.filesDir

        val filename = "my_ghosts.json"
        val ghostFile = File(directory, filename)
        if(!ghostFile.exists())
        {
            ghostFile.createNewFile()
        }
        //TODO delete once dev over
        //For debug purpose to keep clean file
        else{
            ghostFile.delete()
            ghostFile.createNewFile()
        }

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

        btnGhost.setOnClickListener() {
            var ghostnoise = MediaPlayer.create(this, R.raw.ghost);
            ghostnoise.setVolume(1f, 1f)
            ghostnoise.start();
            catchGhosts();
        }

        btnTraining.setOnClickListener() {
            var whistlenoise = MediaPlayer.create(this, R.raw.whistle);
            whistlenoise.setVolume(0.2f, 0.2f)
            whistlenoise.start();
            training();
        }

        btnSpook.setOnClickListener() {
            var kidnoise = MediaPlayer.create(this, R.raw.cri2);
            kidnoise.setVolume(0.2f, 0.2f)
            kidnoise.start();
            spook();
        }

    }

    private fun catchGhosts() {
        val intent = Intent(this, CatchGhostActivity::class.java);
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
        stopService(Intent(this, BackgroundSoundService::class.java))

        // "super" after (continues flow)
        super.onDestroy()
    }
}