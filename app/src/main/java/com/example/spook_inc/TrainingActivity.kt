package com.example.spook_inc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton

import android.widget.Toast

class TrainingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        val btnGhost = findViewById<ImageButton>(R.id.image6)
        val ghost = Ghost(1,"Charlie",10);

        btnGhost.setOnClickListener(){
            //Toast.makeText(this,"Hello Charlie",Toast.LENGTH_SHORT).show()
            //println("Coucou")
            ghost.main()
        }


    }

}

class Ghost(val id: Int, val name: String, val strength: Int)
{
    //val id: Int,
    //val name: String,
    //val strength: Int,
    //val img

    fun main()
    {
        Log.i("Ghost", name)
    }
}
