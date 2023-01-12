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
            "New mediaplayer",
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
            }

        }else {
            mediaPlayer?.start() //supposed to resume
            Toast.makeText(
                applicationContext,
                "Playing Spooky scary skeleton in the Background",
                Toast.LENGTH_SHORT
            ).show()
        }
        return START_STICKY
    }

    private fun onPause()
    {
        mediaPlayer?.pause()
        Toast.makeText(
            applicationContext,
            "Pause",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()


    }


    override fun onLowMemory() {}
}