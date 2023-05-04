package com.example.spook_inc.tools
//https://stackoverflow.com/questions/52810524/how-to-serialized-my-class-objects-using-kotlin
import android.content.Context
import android.media.Image
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
    fun getImageBack() : Int {
        var ghostImgBack = when (this.ghostType) {
            GhostType.TOPHAT -> {
                R.drawable.ghost_tophat_back
            }

            GhostType.MINITOPHAT -> {
                R.drawable.ghost_minitophat_back
            }

            GhostType.NORMAL -> {
                R.drawable.ghost_normal_back
            }

            GhostType.SCYTHE -> {
                R.drawable.ghost_scythe_back
            }

        }
        return ghostImgBack
    }
    fun getImageFront() : Int
    {
        var ghostImgFront = when (this.ghostType) {
            GhostType.TOPHAT -> {
                R.drawable.ghost_tophat_front
            }

            GhostType.MINITOPHAT -> {
                R.drawable.ghost_minitophat_front
            }

            GhostType.NORMAL -> {
                R.drawable.ghost_normal_front
            }

            GhostType.SCYTHE -> {
                R.drawable.ghost_scythe_front
            }
        }
        return ghostImgFront
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

    //Static method
    companion object {
        fun createRandomGhost(): Ghost {
            var name = names[Random.nextInt(1, names.size - 1)]
            var strength = Random.nextInt(10, 250)
            var type = Random.nextInt(0, 4)
            var ghostType = when(type){
                0 -> {
                    GhostType.TOPHAT
                }

                1 -> {
                    GhostType.MINITOPHAT
                }

                2 -> {
                    GhostType.NORMAL
                }

                3 -> {
                    GhostType.SCYTHE
                }

                else -> { // Note the block
                    GhostType.TOPHAT
                }
            }
            return Ghost(name, strength, ghostType)
        }
    }
}

enum class GhostType {
    TOPHAT, MINITOPHAT, NORMAL, SCYTHE
}
