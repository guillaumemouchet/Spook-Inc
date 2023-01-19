package com.example.spook_inc

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class HouseButton (context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageButton(context, attrs) {
    private val paint = Paint()
    private var bitmapStar = BitmapFactory.decodeResource(resources, R.drawable.star)
    private var bitmapStarEmpty = BitmapFactory.decodeResource(resources, R.drawable.grey_star)
    private val difficulty: Int;
    private val maxDifficulty = 3;

    init {
        setBackgroundResource(R.drawable.house_button)
        bitmapStar = Bitmap.createScaledBitmap(bitmapStar, 50, 50, false)
        bitmapStarEmpty = Bitmap.createScaledBitmap(bitmapStarEmpty, 50, 50, false)
        val a = context.obtainStyledAttributes(attrs, R.styleable.HouseButton)
        difficulty = a.getInt(R.styleable.HouseButton_difficulty, 0)
        val objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0f, -5f, 0f)
        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ObjectAnimator.REVERSE
        objectAnimator.start();
        // Add the ripple effect to the button
        background = context.getDrawable(R.drawable.selectable_item_background)
        elevation = resources.getDimension(R.dimen.elevation_button)
        // Add the appropriate iconography
        setImageResource(R.drawable.ic_house)
        contentDescription = context.getString(R.string.house_button)
        // Add appropriate touch feedback
        isClickable = true
        isFocusable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(
                        0, 0, view.width, view.height,
                        resources.getDimension(R.dimen.corner_radius)
                    )
                }
            }
            clipToOutline = true
        }
        // Add hover and focus effects
        stateListAnimator = AnimatorInflater.loadStateListAnimator(
            context, R.animator.button_state_list_animator
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var x = ((width/maxDifficulty - 2*bitmapStar.width)).toFloat();
        val y = ((height - bitmapStar.height)*0.95).toFloat();

        for (i in 0 until maxDifficulty) {
            x += bitmapStar.width + bitmapStar.width/4;
            if(i < difficulty){
                canvas?.drawBitmap(bitmapStar, x.toFloat(), y.toFloat(), paint)
            }
            else{
                canvas?.drawBitmap(bitmapStarEmpty, x.toFloat(), y.toFloat(), paint)
            }
        }
    }

}
