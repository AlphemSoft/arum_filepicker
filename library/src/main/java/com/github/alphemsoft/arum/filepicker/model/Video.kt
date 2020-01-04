package com.github.alphemsoft.arum.filepicker.model

import android.net.Uri
import com.github.alphemsoft.arum.filepicker.enums.ContentType

class Video(
        id: Long,
        uri: Uri,
        name: String,
        extension: String,
        selected: Boolean = false,
        size: Long,
        val duration: Long,
        added: Long
): AbstractFile<Video>(id, uri, ContentType.VIDEO, name, extension, selected, -1, size, added) {
    override fun copy(): Video {
        return Video(
            id,
            uri,
            name,
            extension,
            selected,
            size,
            duration,
            added
        )
    }
}