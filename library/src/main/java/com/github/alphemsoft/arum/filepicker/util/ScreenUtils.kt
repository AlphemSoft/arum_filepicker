package com.github.alphemsoft.arum.filepicker.util

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.view.WindowManager
import android.util.DisplayMetrics
import com.google.android.material.resources.MaterialResources.getDimensionPixelSize




class ScreenUtils(private var context: Context) {
    private var metrics: DisplayMetrics

    val width: Int
        get() = metrics.widthPixels

    val height: Int
        get() = metrics.heightPixels - getStatusBarHeight()

    val realHeight: Int
        get() = metrics.heightPixels / metrics.densityDpi

    val realWidth: Int
        get() = metrics.widthPixels / metrics.densityDpi

    val density: Int
        get() = metrics.densityDpi

    init {
        val wm = context
            .getSystemService(WINDOW_SERVICE) as WindowManager

        val display = wm.defaultDisplay
        metrics = DisplayMetrics()
        display.getMetrics(metrics)

    }

    fun getScale(picWidth: Int): Int {
        val display = (context.getSystemService(WINDOW_SERVICE) as WindowManager)
            .defaultDisplay
        val size = Point()
        var value: Int = size.x / picWidth
        value = (value * 100.0).toInt()
        return value
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}