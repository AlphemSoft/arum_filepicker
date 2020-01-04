package com.github.alphemsoft.arum.filepicker.paging

import androidx.fragment.app.Fragment
import com.github.alphemsoft.arum.filepicker.enums.ContentType

data class ShowableFragment(
    val contentType: ContentType,
    val fragment: Fragment,
    val name: String,
    val icon: Int
)

