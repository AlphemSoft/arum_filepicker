package com.github.alphemsoft.arum.filepicker.decoration

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class RoundedImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        //float radius = 36.0f;
        val clipPath = Path()
        val rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
        clipPath.addRoundRect(rect,
            radius,
            radius, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    companion object {

        var radius = 18.0f
    }
}