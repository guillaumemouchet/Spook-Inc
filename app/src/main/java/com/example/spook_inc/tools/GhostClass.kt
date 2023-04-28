package com.example.spook_inc.tools
//https://stackoverflow.com/questions/52810524/how-to-serialized-my-class-objects-using-kotlin
import android.util.Log
import kotlinx.serialization.Serializable

@Serializable
class Ghost(val id: Int, val name: String, val strength: Int, val ghostType : GhostType)

{
    fun main()
    {
        Log.i("Ghost", name)
        
    }

    fun display()
    {

    }

    override fun equals(other: Any?): Boolean {
        return when(other){
            is Ghost -> {
                this.ghostType == other.ghostType
                        && this.name == other.name
                        && this.strength == other.strength;
            }
            else -> false
        }
    }
}

enum class GhostType {
    TOPHAT, MINITOPHAT, NORMAL, SCYTHE
}
