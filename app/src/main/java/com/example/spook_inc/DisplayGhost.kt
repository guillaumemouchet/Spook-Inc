package com.example.spook_inc

import android.content.Context
import android.media.MediaPlayer
import android.view.View
import android.widget.*

class DisplayGhost(ghost: Ghost, context: Context) : LinearLayout(context) {
    init {

        Toast.makeText(context, "YOLO: ${ghost.name}", Toast.LENGTH_SHORT).show()

        val ghostLayout = findViewById<LinearLayout>(R.id.ghostLayout)
        val layoutHorizontal = LinearLayout(context)
        val layoutVertical = LinearLayout(context)

        layoutVertical.orientation = LinearLayout.VERTICAL
        layoutHorizontal.orientation = LinearLayout.HORIZONTAL

        val btnAddTeam = findViewById<Button>(R.id.btnAdd)

        val imgBtn = ImageButton(context)
        var ghostImg = R.drawable.ghost

        when (ghost.ghostType) {
            Ghost_Type.TOPHAT -> {
                ghostImg = R.drawable.ghost_tophat_front
            }
            Ghost_Type.MINITOPHAT -> {
                ghostImg = R.drawable.ghost_minitophat_front
            }
            Ghost_Type.NORMAL -> {
                ghostImg = R.drawable.ghost_normal_front
            }
            Ghost_Type.SCYTHE -> {
                ghostImg = R.drawable.ghost_scythe_front
            }
            else -> { // Note the block
                ghostImg = R.drawable.ghost_normal_front
            }
        }
        imgBtn.setImageResource(ghostImg)

        imgBtn?.setOnClickListener(){
            btnAddTeam?.visibility = LinearLayout.VISIBLE
            ghost.main()
            btnAddTeam?.text = "Add ${ghost.name}"

            var ghostnoise = MediaPlayer.create(context, R.raw.ghost)
            ghostnoise.setVolume(1f, 1f)
            ghostnoise.start()
        }


        val name = TextView(context)
        name.text = "Name: ${ghost.name}"
        layoutVertical.addView(name)

        val strength = TextView(context)
        strength.text = "Strength: ${ghost.strength}"
        layoutVertical.addView(strength)

        layoutHorizontal.addView(imgBtn)
        layoutHorizontal.addView(layoutVertical)

        ghostLayout.addView(layoutHorizontal)

    }

}

