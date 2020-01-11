package com.github.alphemsoft.arum.filepicker.util

import android.os.Build

fun isQVersionOrHigher(): Boolean{
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}