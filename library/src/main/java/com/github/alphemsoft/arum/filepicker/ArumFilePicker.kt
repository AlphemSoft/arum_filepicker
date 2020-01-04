package com.github.alphemsoft.arum.filepicker

import androidx.fragment.app.FragmentManager
import com.github.alphemsoft.arum.filepicker.provider.OnFileSelectedListener

class ArumFilePicker(listener: OnFileSelectedListener) {

    private val instance: FilePicker =
        FilePicker.getInstance(listener)
    fun show(fragmentManager: FragmentManager){
        instance.show(fragmentManager,
            FilePicker.TAG
        )
    }
}