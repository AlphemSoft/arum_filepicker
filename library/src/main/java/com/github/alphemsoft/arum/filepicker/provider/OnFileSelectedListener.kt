package com.github.alphemsoft.arum.filepicker.provider

import com.github.alphemsoft.arum.filepicker.enums.ContentType
import com.github.alphemsoft.arum.filepicker.model.AbstractFile

interface OnFileSelectedListener {
    fun onFilesReadyListener(files: List<AbstractFile<*>>, contentType: ContentType)
}