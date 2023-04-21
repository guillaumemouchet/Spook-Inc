package com.example.spook_inc

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val REQUEST_CODE = 42
class Camera: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val btnTakePicture = findViewById<Button>(R.id.btnTakePicture)

        btnTakePicture.setOnClickListener()
        {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //security
            if(takePictureIntent.resolveActivity(this.packageManager) != null)
            {
                startActivityForResult(takePictureIntent,REQUEST_CODE)
            }
            else
            {
                Toast.makeText(this,"Error Camera",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imgViewCatchGhost = findViewById<ImageView>(R.id.imgViewCatchGhost)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            val takenImage = data?.extras?.get("data") as Bitmap
            imgViewCatchGhost.setImageBitmap(takenImage)
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}