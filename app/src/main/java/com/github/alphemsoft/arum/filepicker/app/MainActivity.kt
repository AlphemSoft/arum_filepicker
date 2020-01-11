package com.github.alphemsoft.arum.filepicker.app

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.github.alphemsoft.arum.filepicker.ArumFilePicker
import com.github.alphemsoft.arum.filepicker.enums.ContentType
import com.github.alphemsoft.arum.filepicker.model.AbstractFile
import com.github.alphemsoft.arum.filepicker.provider.OnFileSelectedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    OnFileSelectedListener {
    private lateinit var mFilePicker: ArumFilePicker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0f
        bt_open_filepicker.setOnClickListener {
            mFilePicker.show(supportFragmentManager)
        }
        setupFilePicker()
    }

    override fun onFilesReadyListener(files: List<AbstractFile<*>>, contentType: ContentType) {
        Log.d("PickedFiles", "Uri files: ${files.map { it.physicalPath }}, ContentType: $contentType")
    }

    private fun setupFilePicker(){
        val params = ArumFilePicker.Params(
            listener = this,
            genericFileExtensions = arrayOf("pdf"),
            supportedMimeTypes = arrayOf("text/x-java-source", "text/plain")
        )
        mFilePicker = ArumFilePicker(params)
    }
}
