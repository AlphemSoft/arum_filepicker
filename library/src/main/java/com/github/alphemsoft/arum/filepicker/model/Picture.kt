package com.github.alphemsoft.arum.filepicker.model

import android.net.Uri
import com.github.alphemsoft.arum.filepicker.enums.ContentType

class Picture(
        id: Long,
        uri: Uri,
        name: String,
        extension: String,
        size: Long,
        selected: Boolean = false,
        added: Long,
        physicalPath: String?
): AbstractFile<Picture>(id, uri, ContentType.PICTURE, name, extension, selected, -1, size, added, physicalPath) {
    override fun copy(): Picture {
        return Picture(
            id,
            uri,
            name,
            extension,
            size,
            selected,
            added,
            physicalPath
        )
    }
}