package com.example.spook_inc

import android.Manifest

object Constants {
    const val TAG = "cameraX"
    const val FILE_NAME_FORMAT= "dd-MM-yy-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSION = 42 //random
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
}