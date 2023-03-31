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
    /*
     * This Background service helps to play musique in all the application
     * To be sure the musique stops when the player leaves the application it's stopped in all onPause() function
     */
    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.background)
        mediaPlayer?.isLooping = true // Set looping
        mediaPlayer?.setVolume(0.1f, 0.1f)
        mediaPlayer?.start()
        //Toast.makeText(applicationContext,"Start",Toast.LENGTH_SHORT).show()
    }
    /*
     * Since the Service only have a Start and Destroy function the StartCommand has a double purpose
     * It's doing a Start/Pause action, so when we leave a Activity we pause it, and we directly start in when you join the next one
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if(mediaPlayer?.isPlaying == true)
        {
            if(first)
            {
                first = false;
            }else {
                mediaPlayer?.pause()
                //Toast.makeText(applicationContext,"Pause",Toast.LENGTH_SHORT).show()
            }

        }else {
            mediaPlayer?.start() //supposed to resume
            //Toast.makeText(applicationContext,"Resume",Toast.LENGTH_SHORT).show()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }


    override fun onLowMemory() {}
}