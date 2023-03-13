package com.example.spook_inc
//https://stackoverflow.com/questions/52810524/how-to-serialized-my-class-objects-using-kotlin
import android.util.Log
import kotlinx.serialization.Serializable

@Serializable
class Ghost(val id: Int, val name: String, val strength: Int)
{
    fun main()
    {
        Log.i("Ghost", name)
        
    }

    fun display()
    {

    }
}
