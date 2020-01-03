package com.omensoft.arum.filepicker.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.omensoft.arum.filepicker.ArumFilePicker
import com.omensoft.arum.filepicker.enums.ContentType
import com.omensoft.arum.filepicker.model.AbstractFile
import com.omensoft.arum.filepicker.provider.OnFileSelectedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnFileSelectedListener {
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
