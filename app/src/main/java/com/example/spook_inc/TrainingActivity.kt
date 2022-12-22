package com.example.spook_inc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

import android.widget.Toast

class TrainingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        val btnGhost = findViewById<ImageButton>(R.id.image6)

        btnGhost.setOnClickListener(){
            Toast.makeText(this,"Hello charlie",Toast.LENGTH_SHORT).show()
            println("Coucou")
        }
    }

}

class Ghost()
{
    //val id
    // val name
    //val strength
    //val img

    fun main()
    {

    }
}
