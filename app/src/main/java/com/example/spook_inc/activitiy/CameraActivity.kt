package com.example.spook_inc.activitiy

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.spook_inc.R
import com.example.spook_inc.databinding.ActivityCameraBinding
import com.example.spook_inc.tools.Constants
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private var validateImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        outputDirectory = getOutPutDirectory()

        // if permissions' ok, then start the camera
        if(allPermissionsGranted())
        {
            startCamera()
        }
        else
        {
            ActivityCompat.requestPermissions(this, Constants.REQUIRED_PERMISSIONS,Constants.REQUEST_CODE_PERMISSION)
        }

        //The player must take the picture then validate
        binding.btnTakePhoto.setOnClickListener()
        {
            takePhoto()

            if(validateImage)
            {
                binding.btnTakePhoto.setText("Take photo")
                val intent = Intent(this, CatchGhostActivity::class.java);
                startActivity(intent);
            }else {
                binding.btnTakePhoto.setText("Validate photo")
                validateImage = true

            }
        }
    }

    private fun getOutPutDirectory(): File
    {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun takePhoto() {
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        //Save the picture with its URI
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        (this.imageCapture ?: null)?.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val saveUri = Uri.fromFile(photoFile)

                    //Use saveUri in another class
                    val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("uri_key", saveUri.toString()).apply()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError: ${exception.message}", exception)
                }

            }
        )


    }

    private fun startCamera()
    {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also{mPreview ->
                    mPreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            }
            catch (e: Exception) {
                Log.d(Constants.TAG,"startCamera failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
            if (allPermissionsGranted())
                //Toast.makeText(this, "Permission OK", Toast.LENGTH_SHORT).show()
                startCamera()

        } else {
            Toast.makeText(this, "Permission Not OK", Toast.LENGTH_SHORT).show()

            // Stop app
            finish()
        }
    }
    private fun allPermissionsGranted()=
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

}