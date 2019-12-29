package com.omensoft.arum.filepicker.util

class Usable<T>(private val value: T) {
    private var used: Boolean = false

    fun use():T? {
        return if (used){
            null
        }else{
            used = false
            value
        }
    }

    fun peekValue() = value
}