package com.github.alphemsoft.arum.filepicker.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.alphemsoft.arum.filepicker.ArumFilePicker
import com.github.alphemsoft.arum.filepicker.enums.ContentType
import com.github.alphemsoft.arum.filepicker.model.AbstractFile
import com.github.alphemsoft.arum.filepicker.provider.OnFileSelectedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    OnFileSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_open_filepicker.setOnClickListener {
            val filePicker = ArumFilePicker(this)
            filePicker.show(supportFragmentManager)

        }
    }

    override fun onFilesReadyListener(files: List<AbstractFile<*>>, contentType: ContentType) {
        Log.d("PickedFiles", "Uri files: $files, ContentType: $contentType")
    }
}
