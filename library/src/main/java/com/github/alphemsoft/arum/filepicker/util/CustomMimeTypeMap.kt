package com.github.alphemsoft.arum.filepicker.util

object CustomMimeTypeMap {
    private val map: MutableMap<String, String> = HashMap()

    init {
        map["text/x-java-source"] = "java"
    }

    fun getExtensionFromMimeType(mimeType: String): String?{
        return map[mimeType]
    }

    fun hasMimeType(mimeType: String?): Boolean{
        return map.containsKey(mimeType)
    }
}