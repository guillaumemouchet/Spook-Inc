package com.example.spook_inc

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import com.example.spook_inc.R

class BackgroundSoundService : Service() {
    var mediaPlayer: MediaPlayer? = null
    private var first = true
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.background)
        mediaPlayer?.isLooping = true // Set looping
        mediaPlayer?.setVolume(0.1f, 0.1f)
        mediaPlayer?.start()
        Toast.makeText(
            applicationContext,
            "Start of music",
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if(mediaPlayer?.isPlaying == true)
        {
            if(first)
            {
                first = false;
            }else {
                mediaPlayer?.pause()
                Toast.makeText(
                    applicationContext,
                    "Pause music",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }else {
            mediaPlayer?.start() //supposed to resume
            Toast.makeText(
                applicationContext,
                "Resume Spooky scary skeleton in the Background",
                Toast.LENGTH_SHORT
            ).show()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()


    }


    override fun onLowMemory() {}
}