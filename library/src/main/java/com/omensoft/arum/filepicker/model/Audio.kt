package com.omensoft.arum.filepicker.model

import android.net.Uri
import com.omensoft.arum.filepicker.enums.ContentType

class Audio(
        id: Long,
        uri: Uri,
        name: String,
        extension: String,
        selected: Boolean,
        size: Long,
        val duration: Long,
        added: Long
) : AbstractFile<Audio>(id, uri, ContentType.AUDIO, name, extension, selected, -1, size, added) {
    override fun copy(): Audio {
        return Audio(id, uri, name, extension, selected, size, duration, added)
    }


}