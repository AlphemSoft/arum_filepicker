package com.github.alphemsoft.arum.filepicker.model

import android.net.Uri
import com.github.alphemsoft.arum.filepicker.enums.ContentType

class Audio(
        id: Long,
        uri: Uri,
        name: String,
        extension: String,
        selected: Boolean,
        size: Long,
        val duration: Long,
        added: Long,
        physicalPath: String?
) : AbstractFile<Audio>(id, uri, ContentType.AUDIO, name, extension, selected, -1, size, added, physicalPath) {
    override fun copy(): Audio {
        return Audio(
            id,
            uri,
            name,
            extension,
            selected,
            size,
            duration,
            added,
            physicalPath
        )
    }


    override fun equals(other: Any?): Boolean {
        return other is Audio && other.id == id && other.selected == selected && other.added == added
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + duration.hashCode()
        return result
    }
}