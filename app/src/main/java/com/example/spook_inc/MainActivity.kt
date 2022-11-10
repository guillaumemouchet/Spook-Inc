package com.example.spook_inc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCatchGhosts = findViewById<ImageButton>(R.id.imgBtnCatchGhosts)

    }
    fun catchGhosts(){

    }
}