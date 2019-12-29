package com.omensoft.arum.filepicker.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omensoft.arum.filepicker.ArumFilePicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filePicker = ArumFilePicker()
        filePicker.show(supportFragmentManager, "")
    }
}
