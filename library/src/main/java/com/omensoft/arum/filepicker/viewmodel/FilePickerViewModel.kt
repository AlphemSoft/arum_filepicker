package com.omensoft.arum.filepicker.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.omensoft.arum.filepicker.enums.ContentType
import com.omensoft.arum.filepicker.model.*
import com.omensoft.arum.filepicker.util.Usable
import kotlin.collections.ArrayList

class FilePickerViewModel: ViewModel(){

    val currentContentType = MutableLiveData<ContentType>()
    val permissionState: MutableLiveData<Int> = MutableLiveData()
    val audios: MutableLiveData<List<Audio>> = MutableLiveData()
    val pictures: MutableLiveData<List<Picture>> = MutableLiveData()
    val videos: MutableLiveData<List<Video>> = MutableLiveData()
    val genericFiles: MutableLiveData<List<GenericFile>> = MutableLiveData()

    val showablePictures = Transformations.map(pictures, ::transformFiles)
    val showableAudios = Transformations.map(audios, ::transformFiles)
    val showableVideos = Transformations.map(videos, ::transformFiles)
    val showableGenericFiles = Transformations.map(genericFiles, ::transformFiles)

    val selectedPictures = Transformations.map(pictures, ::transformSelectedFiles)
    val selectedAudios = Transformations.map(audios, ::transformSelectedFiles)
    val selectedVideos = Transformations.map(videos, ::transformSelectedFiles)
    val selectedGenericFiles = Transformations.map(genericFiles, ::transformSelectedFiles)

    private fun transformSelectedFiles(fileList: List<AbstractFile<*>>?): List<AbstractFile<*>>{
        val selectedFiles: List<Uri>? = getSelectedFileList(fileList)
        return fileList?.filter {
            selectedFiles?.contains(it.uri)?:false
        }?:ArrayList()
    }

    private fun getSelectedFileList(fileList: List<AbstractFile<*>>?): List<Uri>? {
        var selectedFiles: List<Uri>? = null
        fileList?.let {
            if (fileList.isNotEmpty()) {
                selectedFiles = when (fileList[0].contentType) {
                    ContentType.PICTURE -> _selectedPictures
                    ContentType.VIDEO -> _selectedVideos
                    ContentType.AUDIO -> _selectedAudios
                    ContentType.GENERIC_FILE -> _selectedGenericFiles
                }
            }
        }
        return selectedFiles
    }

    private fun transformFiles(files: List<AbstractFile<*>>?): List<AbstractFile<*>>{
        val mutableList = mutableListOf<AbstractFile<*>>()
        files?.forEach {
            mutableList.add(it.copy())
        }

        val result = ArrayList<AbstractFile<*>>()
        val selectedUriFiles = getSelectedFileList(files)
        val selectedFiles = mutableListOf<AbstractFile<*>>()
        result.addAll(mutableList.minus(selectedFiles))

        selectedUriFiles?.forEachIndexed {index: Int, uri: Uri ->
            mutableList.find { it.uri == uri }?.let {
                it.selected = true
                it.selectedIndex = index
                selectedFiles.add(it)
            }
        }
        result.sortWith(compareBy {it.id})
        return result
    }

    private val _selectedPictures: MutableList<Uri> = ArrayList()
    private val _selectedVideos: MutableList<Uri> = ArrayList()
    private val _selectedAudios: MutableList<Uri> = ArrayList()
    private val _selectedGenericFiles: MutableList<Uri> = ArrayList()

    fun markItemAsSelected(file: AbstractFile<*>) {

        val list: MutableLiveData<out List<AbstractFile<*>>>
        val selectedList: MutableList<Uri>
        when(file.contentType){
            ContentType.PICTURE -> {
                selectedList = _selectedPictures
                list = pictures
            }
            ContentType.AUDIO -> {
                selectedList = _selectedAudios
                list = audios
            }
            ContentType.VIDEO -> {
                selectedList = _selectedVideos
                list = videos
            }
            ContentType.GENERIC_FILE -> {
                selectedList = _selectedGenericFiles
                list = genericFiles
            }
        }

        if (selectedList.contains(file.uri)){
            selectedList.remove(file.uri)
        }else{
            selectedList.add(file.uri)
        }

        list.value = list.value

    }
}