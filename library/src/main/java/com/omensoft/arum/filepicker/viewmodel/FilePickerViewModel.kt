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
    val permissionState: MutableLiveData<Usable<Int>> = MutableLiveData()
    val audios: MutableLiveData<List<Audio>> = MutableLiveData()
    val pictures: MutableLiveData<List<Picture>> = MutableLiveData()
    val documents: MutableLiveData<List<Document>> = MutableLiveData()
    val videos: MutableLiveData<List<Video>> = MutableLiveData()

    init {
        permissionState.value = null
    }

    val showablePictures = Transformations.map(pictures, ::transformFiles)
    val showableAudios = Transformations.map(audios, ::transformFiles)
    val showableDocuments = Transformations.map(documents, ::transformFiles)
    val showableVideos = Transformations.map(videos, ::transformFiles)

    val selectedPictures = Transformations.map(pictures, ::transformSelectedPictures)
    val selectedAudios = Transformations.map(audios, ::transformSelectedPictures)
    val selectedDocuments = Transformations.map(documents, ::transformSelectedPictures)
    val selectedVideos = Transformations.map(videos, ::transformSelectedPictures)

    private fun transformSelectedPictures(fileList: List<AbstractFile<*>>?): List<AbstractFile<*>>{
        return fileList?.filter {
            selectedFiles.contains(it.uri)
        }?:ArrayList()
    }

    private fun transformFiles(files: List<AbstractFile<*>>?): List<AbstractFile<*>>{
        val mutableList = mutableListOf<AbstractFile<*>>()
        files?.forEach {
            mutableList.add(it.copy())
        }

        val result = ArrayList<AbstractFile<*>>()
        val selected = mutableList.filter { picture ->
            selectedFiles.contains(picture.uri)
        }
        result.addAll(mutableList.minus(selected))
        selected.forEachIndexed{index: Int, itemSelected: AbstractFile<*> ->
            itemSelected.selected = true
            itemSelected.selectedIndex = index
        }
        result.addAll(selected)
        result.sortWith(compareBy {it.id})
        return result
    }

    private val selectedFiles: MutableList<Uri> = ArrayList()

    fun markItemAsSelected(file: AbstractFile<*>) {
        if (selectedFiles.contains(file.uri)){
            selectedFiles.remove(file.uri)
        }else{
            selectedFiles.add(file.uri)
        }
        when(file.contentType){
            ContentType.PICTURE -> pictures.value = pictures.value
            ContentType.AUDIO -> audios.value = audios.value
            ContentType.VIDEO -> videos.value = videos.value
            ContentType.DOCUMENT -> documents.value = documents.value
        }

    }
}