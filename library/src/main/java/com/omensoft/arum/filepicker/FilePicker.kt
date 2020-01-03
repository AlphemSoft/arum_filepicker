package com.omensoft.arum.filepicker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.omensoft.arum.filepicker.databinding.FragmentFilepickerBinding
import com.omensoft.arum.filepicker.enums.ContentType
import com.omensoft.arum.filepicker.model.Audio
import com.omensoft.arum.filepicker.model.GenericFile
import com.omensoft.arum.filepicker.model.Picture
import com.omensoft.arum.filepicker.model.Video
import com.omensoft.arum.filepicker.paging.ContentPageAdapter
import com.omensoft.arum.filepicker.paging.ShowableFragment
import com.omensoft.arum.filepicker.provider.FileProvider
import com.omensoft.arum.filepicker.provider.OnFileSelectedListener
import com.omensoft.arum.filepicker.util.ScreenUtils
import com.omensoft.arum.filepicker.viewmodel.FilePickerViewModel


class FilePicker internal constructor(): BottomSheetDialogFragment(), FileProvider.FileProviderCallback {

    private lateinit var listener: OnFileSelectedListener
    private lateinit var mViewModel: FilePickerViewModel
    private lateinit var mDataBinding: FragmentFilepickerBinding
    private lateinit var fileProvider: FileProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(activity!!).get(FilePickerViewModel::class.java)
        mDataBinding = FragmentFilepickerBinding.inflate(inflater, container, false)
        fileProvider = FileProvider.getInstance(
            this.viewLifecycleOwner,
            this.activity!!,
            this
        )
        setupViews()
        setupExpandedState()
        setupPages()
        setupObservers()
        return mDataBinding.root
    }

    private fun setupViews() {
        mDataBinding.viewSpace.setOnClickListener {
            dismiss()
        }
    }

    private fun setupExpandedState() {
        val screenUtils = ScreenUtils(context!!)
        val height = screenUtils.height
        val translucentColor = ContextCompat.getColor(context!!, android.R.color.transparent)
        (mDataBinding.root).setBackgroundColor(translucentColor)
        mDataBinding.root.viewTreeObserver?.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog?
            val bottomSheet = dialog!!.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            if (behavior.state != BottomSheetBehavior.STATE_EXPANDED){
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = height
            }
        }

        mDataBinding.clContainer.minHeight = height
    }

    private fun setupPages() {
        activity?.let {
            val contentPageAdapter = ContentPageAdapter(it)
            val pages = ArrayList<ShowableFragment>()
            pages.add(ShowableFragment(ContentType.PICTURE, ContentPageFragment.getInstance(ContentType.PICTURE), "PictureFragment", R.drawable.ic_image))
            pages.add(ShowableFragment(ContentType.VIDEO, ContentPageFragment.getInstance(ContentType.VIDEO), "VideoFragment", R.drawable.ic_video))
            pages.add(ShowableFragment(ContentType.AUDIO, ContentPageFragment.getInstance(ContentType.AUDIO), "AudioFragment", R.drawable.ic_music_circle))
            pages.add(ShowableFragment(ContentType.GENERIC_FILE, ContentPageFragment.getInstance(ContentType.GENERIC_FILE), "DocumentFragment", R.drawable.ic_file_document_box))
            contentPageAdapter.addPages(pages)
            mDataBinding.vp2Pages.adapter = contentPageAdapter
            mDataBinding.vp2Pages.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){

                override fun onPageSelected(position: Int) {
                    mViewModel.currentContentType.value = pages[position].contentType
                }
            })
            mViewModel.currentContentType.value = pages[0].contentType
            val icons = pages.map {showableFragment->
                ContextCompat.getDrawable(context?.applicationContext!!, showableFragment.icon)
            }
            val mediator = TabLayoutMediator(mDataBinding.tabLayout, mDataBinding.vp2Pages, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.icon = icons[position]
                if (position == 0){
                    tab.select()
                }
            })
            mediator.attach()
        }

    }

    private fun setupObservers() {
        mViewModel.selectedPictures.observe(this, Observer {
            it?.let { safe->
                mDataBinding.fabSend.visibility = if (safe.isEmpty()) View.GONE else View.VISIBLE
            }
        })

        mViewModel.currentContentType.observe(this, Observer {
            it?.let { safeContentType->
                val selectedList = when(safeContentType){
                    ContentType.PICTURE -> mViewModel.selectedPictures
                    ContentType.AUDIO -> mViewModel.selectedAudios
                    ContentType.VIDEO -> mViewModel.selectedVideos
                    ContentType.GENERIC_FILE -> mViewModel.selectedGenericFiles
                }
                selectedList.observe(this, Observer {list->
                    list?.let {safeSelectedList->
                        mDataBinding.fabSend.visibility = if (safeSelectedList.isEmpty()) View.GONE else View.VISIBLE
                        mDataBinding.fabSend.setOnClickListener {
                            listener.onFilesReadyListener(safeSelectedList, safeContentType)
                            dismiss()
                        }
                    }
                })
            }
        })
    }

    override fun onPermissionChange(permissionState: FileProvider.PermissionState) {
        mViewModel.permissionState.value = permissionState.state
    }

    override fun dispatchAudioList(resources: List<Audio>) {
        mViewModel.audios.value = resources
    }

    override fun dispatchDocumentList(resources: List<GenericFile>) {
        mViewModel.genericFiles.value = resources
    }

    override fun dispatchPictureList(resources: List<Picture>) {
        mViewModel.pictures.value = resources
    }

    override fun dispatchVideoList(resources: List<Video>) {
        mViewModel.videos.value = resources
    }

    internal companion object{
        const val TAG = "ARUM_FILE_PICKER_INSTANCE"
        fun getInstance(
            listener: OnFileSelectedListener
        ) = FilePicker().apply {
            this.listener = listener
        }
    }
}
