package com.example.spook_inc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGhost = findViewById<ImageButton>(R.id.imgBtnGhost)

        val flashLightOff = findViewById<ImageButton>(R.id.imgBtnCatchGhostsOff)
        //val flashLightOn = findViewById<ImageButton>(R.id.imgBtnCatchGhostsOff)

        val btnTraining = findViewById<Button>(R.id.btnTraining)
        val btnSpook = findViewById<Button>(R.id.btnSpook)

        var lightOff = true

        flashLightOff.setOnClickListener(){
            if(lightOff){
                flashLightOff.setImageResource(R.drawable.flashlight_on);
                btnGhost.isVisible = true;
                lightOff = false;
            }else{
                flashLightOff.setImageResource(R.drawable.flashlight_off);
                btnGhost.isVisible = false;
                lightOff = true;
            }


        }



    }
    fun catchGhosts(){

    }

}