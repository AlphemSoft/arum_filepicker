package com.omensoft.arum.filepicker.provider

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentResolverCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.omensoft.arum.filepicker.model.Audio
import com.omensoft.arum.filepicker.model.GenericFile
import com.omensoft.arum.filepicker.model.Picture
import com.omensoft.arum.filepicker.model.Video
class FileProvider private constructor(
        private val mActivity: FragmentActivity,
        private val fileProviderCallback: FileProviderCallback
): LifecycleObserver{

    init {
        Log.d("ProviderInstance", toString())
    }

    enum class PermissionState(val state: Int){
        DENIED(0),
        GRANTED(1),
        SHOULD_SHOW_RATIONALE(2)
    }

    interface FileProviderCallback{
        fun onPermissionChange(permissionState: PermissionState)
        fun dispatchAudioList(resources: List<Audio>)
        fun dispatchDocumentList(resources: List<GenericFile>)
        fun dispatchPictureList(resources: List<Picture>)
        fun dispatchVideoList(resources: List<Video>)
    }

    companion object{
        private const val REQUEST_CODE = 4991
        private var mInstance: FileProvider? = null
        fun getInstance(
            lifecycleOwner: LifecycleOwner,
            fragmentActivity: FragmentActivity,
            fileProviderCallback: FileProviderCallback
        ): FileProvider {
            mInstance ?:run {
                mInstance =
                    FileProvider(
                        fragmentActivity,
                        fileProviderCallback
                    )
            }
            lifecycleOwner.lifecycle.addObserver(mInstance!!)
            return mInstance!!
        }

        fun getInstance(): FileProvider? {
            return mInstance ?:throw IllegalStateException("Doesn't exists a created instance")
        }
    }

    init {
//        checkForReadStoragePermission()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun checkForReadStoragePermission(){
        when(ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            PackageManager.PERMISSION_GRANTED->  {
                fileProviderCallback.onPermissionChange(PermissionState.GRANTED)
                providePictures()
                provideAudios()
                provideVideos()
                provideMisc()
            }
            PackageManager.PERMISSION_DENIED->  {
                fileProviderCallback.onPermissionChange(PermissionState.DENIED)
            }
            else -> {
                fileProviderCallback.onPermissionChange(PermissionState.DENIED)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun stop(){
        mActivity.lifecycle.removeObserver(this)
//        mInstance = null
    }

    @SuppressLint("InlinedApi")
    private fun provideAudios() {
        val audioList = mutableListOf<Audio>()

        val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATE_ADDED
        )

        val legacyQuery = ContentResolverCompat.query(
                mActivity.contentResolver,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null,
                null
        )

        legacyQuery?.let { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val durationColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val addedColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getLong(sizeColumn)
                val duration = cursor.getLong(durationColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                )
                val added = cursor.getLong(addedColumn)
                audioList.add(Audio(id, contentUri, name, "none", false, size, duration, added))
            }
        }

        fileProviderCallback.dispatchAudioList(audioList.toList())
    }

    @SuppressLint("InlinedApi")
    private fun provideVideos() {
        val audioList = mutableListOf<Video>()

        val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED
        )

        val legacyQuery = ContentResolverCompat.query(
                mActivity.contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null,
                null
        )

        legacyQuery?.let { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val durationColumn= cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val addedColumn= cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getLong(sizeColumn)
                val duration = cursor.getLong(durationColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                )
                val added = cursor.getLong(addedColumn)
                audioList.add(Video(id, contentUri, name, "none", false, size, duration, added))
            }
        }

        fileProviderCallback.dispatchVideoList(audioList.toList())
    }

    @SuppressLint("InlinedApi")
    private fun provideMisc() {
        val miscList = mutableListOf<GenericFile>()

        val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MIME_TYPE
        )

        val supportedFileTypes =
                arrayOf("doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf")
        val selectionArgs = arrayOfNulls<String>(supportedFileTypes.size)
        val selectionBuilder = StringBuilder()

        for (index in supportedFileTypes.indices){
            selectionBuilder.append(MediaStore.Files.FileColumns.MIME_TYPE)
            selectionBuilder.append("=?")
            selectionArgs[index] = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(supportedFileTypes[index])
            if (index < supportedFileTypes.size - 1){
                selectionBuilder.append(" OR ")
            }
        }

        val legacyQuery = ContentResolverCompat.query(
                mActivity.contentResolver,
                MediaStore.Files.getContentUri("external"),
                projection,
                selectionBuilder.toString(),
                selectionArgs,
                null,
                null
        )

        legacyQuery?.let { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val addedColumn= cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
            val mimeTypeColumn= cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getLong(sizeColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Files.getContentUri("external"),
                        id
                )
                val added = cursor.getLong(addedColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                miscList.add(GenericFile(id, contentUri, name, "none", false, size, added, mimeType))
            }
        }

        fileProviderCallback.dispatchDocumentList(miscList.toList())
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(mActivity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    private fun startFileProviders(){
        providePictures()
    }
    
    private fun providePictures(){
        val pictureList = mutableListOf<Picture>()

        val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED
        )

        val legacyQuery = ContentResolverCompat.query(
                mActivity.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null,
                null
        )

        legacyQuery?.let { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val addedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getLong(sizeColumn)
                val added = cursor.getLong(addedColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                )
                pictureList.add(Picture(id, contentUri, name, "none", size, false, added))
            }
        }

        fileProviderCallback.dispatchPictureList(pictureList.toList())
    }
}