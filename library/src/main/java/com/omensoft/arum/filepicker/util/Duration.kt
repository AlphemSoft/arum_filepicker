package com.omensoft.arum.filepicker.util

class Duration(var longDuration: Long) {
    var hour: Long = 0
        private set
    var minutes: Long = 0
        private set
    var seconds: Long = 0
        private set

    init {
        longDuration /= 1000
        if (longDuration > 0) {
            seconds = longDuration % 60
            longDuration /= 60
        }
        if (longDuration > 0) {
            minutes = longDuration % 60
            longDuration /= 60
        }
        if (longDuration > 0) {
            hour = longDuration
        }
    }

    override fun toString(): String {
        return when {
            hour == 0L -> {
                String.format("%02d:%02d", minutes, seconds)
            }
            hour == 0L && minutes == 0L -> {
                String.format("%02d", minutes)
            }
            else -> {
                String.format("%02d:%02d:%02d", hour, minutes, seconds)
            }
        }
    }

    override fun equals(obj: Any?): Boolean {
        var other: Duration? = null
        if (obj is Duration) {
            other = obj
        } else {
            return false
        }
        return other.hour == hour && other.minutes == minutes && other.seconds == seconds
    }
}