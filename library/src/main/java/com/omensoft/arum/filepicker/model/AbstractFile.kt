package com.omensoft.arum.filepicker.model

import android.net.Uri
import com.omensoft.arum.filepicker.enums.ContentType

abstract class AbstractFile<out T: AbstractFile<T>>(
        val id: Long,
        val uri: Uri,
        val contentType: ContentType,
        val name: String,
        val extension: String,
        var selected: Boolean,
        var selectedIndex: Int,
        val size: Long,
        val added: Long
): Comparable<AbstractFile<*>>{
    override fun toString(): String {
        return "Uri: $uri, Selected: $selected, Index: $selectedIndex"
    }

    override fun compareTo(other: AbstractFile<*>): Int {
        return when{
            other.id > id -> 1
            other.id < id -> -1
            other.id == id -> 0
            else -> -1
        }
    }

    abstract fun copy(): T

    override fun equals(other: Any?): Boolean {
        return other is AbstractFile<*> && other.id == id && other.selected == selected && other.selectedIndex == selectedIndex && other.added == added
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + extension.hashCode()
        result = 31 * result + selected.hashCode()
        result = 31 * result + selectedIndex
        result = 31 * result + size.hashCode()
        result = 31 * result + added.hashCode()
        return result
    }
}