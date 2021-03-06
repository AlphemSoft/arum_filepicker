package com.omensoft.arum.filepicker.model

import android.net.Uri
import com.omensoft.arum.filepicker.enums.ContentType

class Picture(
        id: Long,
        uri: Uri,
        name: String,
        extension: String,
        size: Long,
        selected: Boolean = false,
        added: Long
): AbstractFile<Picture>(id, uri, ContentType.PICTURE, name, extension, selected, -1, size, added) {
    override fun copy(): Picture {
        return Picture(id, uri, name, extension, size, selected, added)
    }
}