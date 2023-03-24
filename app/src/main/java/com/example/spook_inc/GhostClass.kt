package com.example.spook_inc

import android.util.Log

class Ghost(val id: Int, val name: String, val strength: Int, val ghostType : Ghost_Type )
{

    fun main()
    {
        Log.i("Ghost", name)
    }

    fun display()
    {

    }
}

enum class Ghost_Type {
    TOPHAT, MINITOPHAT, NORMAL, SCYTHE
}
