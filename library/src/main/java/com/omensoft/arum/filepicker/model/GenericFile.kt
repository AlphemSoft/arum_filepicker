package com.omensoft.arum.filepicker.model

import android.net.Uri
import com.omensoft.arum.filepicker.enums.ContentType

class Document(
        id: Long,
        uri: Uri,
        name: String,
        extension: String,
        selected: Boolean,
        size: Long,
        added: Long
): AbstractFile<Document>(id, uri, ContentType.DOCUMENT, name, extension, selected, -1, size, added) {
    override fun copy(): Document {
        return Document(id, uri, name, extension, selected, size, added)
    }
}