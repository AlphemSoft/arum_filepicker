package com.github.alphemsoft.arum.filepicker.util.extension

import android.os.Build
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.tabs.TabLayout

fun TabLayout.Tab.setIconColor(@ColorRes color: Int){
    require(parent?.context != null){"Null Context"}
    val colorValue = ContextCompat.getColor(parent?.context!!, color)
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
        icon?.setTint(colorValue)
    } else {
        icon?.let {
            DrawableCompat.setTint(it, colorValue)
        }
    }
}