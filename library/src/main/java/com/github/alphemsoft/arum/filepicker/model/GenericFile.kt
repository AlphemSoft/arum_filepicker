package com.github.alphemsoft.arum.filepicker.model

import android.net.Uri
import com.github.alphemsoft.arum.filepicker.enums.ContentType

class GenericFile(
        id: Long,
        uri: Uri,
        name: String,
        extension: String,
        selected: Boolean,
        size: Long,
        added: Long,
        val mimeType: String
): AbstractFile<GenericFile>(id, uri, ContentType.GENERIC_FILE, name, extension, selected, -1, size, added) {
    override fun copy(): GenericFile {
        return GenericFile(
            id,
            uri,
            name,
            extension,
            selected,
            size,
            added,
            mimeType
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is GenericFile && other.id == id && other.selected == selected && other.added == added
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }


}