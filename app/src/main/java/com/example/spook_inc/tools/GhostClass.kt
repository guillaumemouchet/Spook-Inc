package com.example.spook_inc.tools
//https://stackoverflow.com/questions/52810524/how-to-serialized-my-class-objects-using-kotlin
import android.content.Context
import android.provider.Settings.System.getString
import android.util.Log
import com.example.spook_inc.R
import com.example.spook_inc.activitiy.MainActivity
import kotlinx.serialization.Serializable
import kotlin.random.Random

private var names: List<String> = mutableListOf("Harlee Hinton","Bryant Choi","Megan Yu","Grayson Velez","Lauryn Wright","Magnus Wiley","Alianna Gentry","Dalton Gillespie","Rose Tate","Kellan Vasquez","Danna Arellano","Kristopher Barton","Atticus Ware", "Chandra Nalaar", "Jace Beleren", "Nissa Revane", "Liliana Vess", "Ajani Goldmane","Ellen Stark","Santino Sexton","Eileen Newton")

@Serializable
class Ghost(val name: String, val strength: Int, val ghostType : GhostType)

{
    fun main()
    {
        Log.i("Ghost", name)
        
    }

    fun display()
    {

    }

    companion object {
        fun createRandomGhost(): Ghost {
            var name_id = Random.nextInt(1, names.size - 1)
            var name = names[name_id]
            var strength = Random.nextInt(10, 250)
            var type = Random.nextInt(0, 4)
            var ghostType = GhostType.TOPHAT
            when (type) {
                0 -> {
                    ghostType = GhostType.TOPHAT
                }

                1 -> {
                    ghostType = GhostType.MINITOPHAT
                }

                2 -> {
                    ghostType = GhostType.NORMAL
                }

                3 -> {
                    ghostType = GhostType.SCYTHE
                }

                else -> { // Note the block
                    ghostType = GhostType.TOPHAT
                }
            }
            return Ghost(name, strength, ghostType)
        }
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
