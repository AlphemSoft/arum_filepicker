package com.github.alphemsoft.arum.filepicker.util.extension

import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat

fun AppCompatCheckBox.changeStateColorWithRes(@ColorRes colorStateRes: Int){
    val colorStateList = ContextCompat.getColorStateList(context, colorStateRes)
    supportButtonTintList = colorStateList
}