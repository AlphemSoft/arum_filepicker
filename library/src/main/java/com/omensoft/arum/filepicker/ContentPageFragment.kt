package com.omensoft.arum.filepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.omensoft.arum.filepicker.databinding.FragmentContentPageBinding
import com.omensoft.arum.filepicker.decoration.FairSpaceItemDecoration
import com.omensoft.arum.filepicker.enums.ContentType
import com.omensoft.arum.filepicker.enums.ContentType.*
import com.omensoft.arum.filepicker.list.FileAdapter
import com.omensoft.arum.filepicker.model.AbstractFile
import com.omensoft.arum.filepicker.viewmodel.FilePickerViewModel
import java.util.*

class ContentPageFragment: Fragment(), FileAdapter.OnItemSelected {
    private lateinit var mDataBinding: FragmentContentPageBinding

    private lateinit var mViewModel: FilePickerViewModel
    private lateinit var fileProvider: FileProvider
    private lateinit var mContentType: ContentType
    private lateinit var mFileAdapter: FileAdapter
    companion object {

        fun getInstance(contentType: ContentType): ContentPageFragment =
                ContentPageFragment().apply {
                    mContentType = contentType
                }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this.activity!!)[FilePickerViewModel::class.java]
        mDataBinding = FragmentContentPageBinding.inflate(inflater, container, false)
        setupViews()
        setupObservers()
        setupStateChangers()
        return mDataBinding.root
    }

    private fun setupViews() {
        mFileAdapter = FileAdapter(this)
        mDataBinding.rvContent.adapter = mFileAdapter
        (mDataBinding.rvContent.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mDataBinding.rvContent.layoutManager = when(mContentType){
            DOCUMENT,AUDIO ->
                LinearLayoutManager(this.activity, RecyclerView.VERTICAL, false)
            PICTURE, VIDEO ->
                GridLayoutManager(this.activity, 3)
        }
        if (mContentType == PICTURE || mContentType == VIDEO){
            val fairSpaceItemDecoration =
                    FairSpaceItemDecoration(20, 3, true)
            mDataBinding.rvContent.addItemDecoration(fairSpaceItemDecoration)
        }
    }

    private fun setupObservers() {
        mViewModel.permissionState.observe(this, Observer {
            it?.use()?.let {safePermissionState ->
                when(safePermissionState){
                    FileProvider.PermissionState.GRANTED.state->{
                        mDataBinding.groupRequireReadPermission.visibility = View.GONE
                        mDataBinding.rvContent.visibility = View.VISIBLE
                    }
                    FileProvider.PermissionState.DENIED.state ->{
                        mDataBinding.groupRequireReadPermission.visibility = View.VISIBLE
                        mDataBinding.rvContent.visibility = View.GONE
                    }
                }
            }
        })

        val fileList = when(mContentType) {
            PICTURE -> mViewModel.showablePictures
            AUDIO -> mViewModel.showableAudios
            VIDEO -> mViewModel.showableVideos
            DOCUMENT -> mViewModel.showableDocuments
        }

        fileList.observe(this, Observer {
            it?.let { fileList->
                mFileAdapter.addContent(fileList)
            }
        })

    }

    private fun setupStateChangers() {
        fileProvider = FileProvider.getInstance()
        mDataBinding.btPermissionGrant.setOnClickListener {
            fileProvider.requestPermission()
        }
    }

    override fun onItemSelected(file: AbstractFile<*>) {
        mViewModel.markItemAsSelected(file)
    }
}
