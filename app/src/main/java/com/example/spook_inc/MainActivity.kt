package com.example.spook_inc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
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

        var imgForeground = findViewById<ImageView>(R.id.imgForeground)

        var lightOff = true

        flashLightOff.setOnClickListener(){
            if(lightOff){
                flashLightOff.setImageResource(R.drawable.flashlight_on);
                btnGhost.isVisible = true;
                btnTraining.isVisible = true;
                btnSpook.isVisible = true;
                imgForeground.alpha = 0.0f;
                lightOff = false;
            }else{
                flashLightOff.setImageResource(R.drawable.flashlight_off);
                btnGhost.isVisible = false;
                btnTraining.isVisible = false;
                btnSpook.isVisible = false;
                imgForeground.alpha = 0.8f;
                lightOff = true;
            }
        }

        btnGhost.setOnClickListener(){
            catchGhosts();
        }

        btnTraining.setOnClickListener(){
            training();
        }

        btnSpook.setOnClickListener(){
            spook();
        }

    }
    fun catchGhosts(){
        val intent = Intent(this, CatchGhostActivity::class.java);
        startActivity(intent);
    }

    fun training(){
        val intent = Intent(this, TrainingActivity::class.java);
        startActivity(intent);
    }

    fun spook(){
        val intent = Intent(this, SpookActivity::class.java);
        startActivity(intent);
    }

}