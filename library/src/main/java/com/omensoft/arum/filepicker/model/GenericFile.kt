package com.omensoft.arum.filepicker.model

import android.net.Uri
import com.omensoft.arum.filepicker.enums.ContentType

class GenericFile(
        id: Long,
        uri: Uri,
        name: String,
        extension: String,
        selected: Boolean,
        size: Long,
        added: Long,
        val mimeType: String
): AbstractFile<GenericFile>(id, uri, ContentType.DOCUMENT, name, extension, selected, -1, size, added) {
    override fun copy(): GenericFile {
        return GenericFile(id, uri, name, extension, selected, size, added, mimeType)
    }
}