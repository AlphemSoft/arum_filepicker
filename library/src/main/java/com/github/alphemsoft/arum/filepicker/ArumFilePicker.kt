package com.github.alphemsoft.arum.filepicker

import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import com.github.alphemsoft.arum.filepicker.provider.OnFileSelectedListener

class ArumFilePicker(params: Params) {

    private val instance: FilePickerBottomSheet =
        FilePickerBottomSheet.getInstance(params)

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    fun show(fragmentManager: FragmentManager){
        if (instance.isAdded){
            instance.dismiss()
        }
        instance.show(fragmentManager,
            FilePickerBottomSheet.TAG
        )
    }

    data class Params(
        val listener: OnFileSelectedListener,
        val genericFileExtensions: Array<String>? = null,
        val supportedMimeTypes: Array<String>? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Params

            if (listener != other.listener) return false
            if (genericFileExtensions != null) {
                if (other.genericFileExtensions == null) return false
                if (!genericFileExtensions.contentEquals(other.genericFileExtensions)) return false
            } else if (other.genericFileExtensions != null) return false
            if (supportedMimeTypes != null) {
                if (other.supportedMimeTypes == null) return false
                if (!supportedMimeTypes.contentEquals(other.supportedMimeTypes)) return false
            } else if (other.supportedMimeTypes != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = listener.hashCode()
            result = 31 * result + (genericFileExtensions?.contentHashCode() ?: 0)
            result = 31 * result + (supportedMimeTypes?.contentHashCode() ?: 0)
            return result
        }
    }
}