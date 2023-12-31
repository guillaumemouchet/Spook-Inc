package com.example.spook_inc.my_widgets

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.example.spook_inc.R

class HouseButton (context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageButton(context, attrs) {
    private val paint = Paint()
    private var bitmapStar = BitmapFactory.decodeResource(resources, R.drawable.star)
    private var bitmapStarEmpty = BitmapFactory.decodeResource(resources, R.drawable.grey_star)
    private val difficulty: Int
    private val maxDifficulty = 3

    init {
        setBackgroundResource(R.drawable.house_button)
        bitmapStar = Bitmap.createScaledBitmap(bitmapStar, 50, 50, false)
        bitmapStarEmpty = Bitmap.createScaledBitmap(bitmapStarEmpty, 50, 50, false)
        val a = context.obtainStyledAttributes(attrs, R.styleable.HouseButton)
        difficulty = a.getInt(R.styleable.HouseButton_difficulty, 0)
        a.recycle()
        val objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0f, -5f, 0f)
        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ObjectAnimator.REVERSE
        objectAnimator.start()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var x = ((width/maxDifficulty - 2*bitmapStar.width)).toFloat()
        val y = ((height - bitmapStar.height)*0.95).toFloat()

        for (i in 0 until maxDifficulty) {
            x += bitmapStar.width + bitmapStar.width/4
            if(i < difficulty){
                canvas?.drawBitmap(bitmapStar, x, y, paint)
            }
            else{
                canvas?.drawBitmap(bitmapStarEmpty, x, y, paint)
            }
        }
    }

}
