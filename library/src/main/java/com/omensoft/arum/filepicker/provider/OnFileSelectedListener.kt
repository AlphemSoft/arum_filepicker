package com.omensoft.arum.filepicker.provider

import com.omensoft.arum.filepicker.enums.ContentType
import com.omensoft.arum.filepicker.model.AbstractFile

interface OnFileSelectedListener {
    fun onFilesReadyListener(files: List<AbstractFile<*>>, contentType: ContentType)
}