package com.github.alphemsoft.arum.filepicker

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.github.alphemsoft.arum.filepicker.databinding.FragmentFilepickerBinding
import com.github.alphemsoft.arum.filepicker.enums.ContentType
import com.github.alphemsoft.arum.filepicker.model.Audio
import com.github.alphemsoft.arum.filepicker.model.GenericFile
import com.github.alphemsoft.arum.filepicker.model.Picture
import com.github.alphemsoft.arum.filepicker.model.Video
import com.github.alphemsoft.arum.filepicker.paging.ContentPageAdapter
import com.github.alphemsoft.arum.filepicker.paging.ShowableFragment
import com.github.alphemsoft.arum.filepicker.provider.FileProvider
import com.github.alphemsoft.arum.filepicker.util.ScreenUtils
import com.github.alphemsoft.arum.filepicker.util.extension.setIconColor
import com.github.alphemsoft.arum.filepicker.viewmodel.FilePickerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class FilePickerBottomSheet internal constructor(): BottomSheetDialogFragment(), FileProvider.FileProviderCallback {

    private lateinit var mViewModel: FilePickerViewModel
    private lateinit var mDataBinding: FragmentFilepickerBinding
    private lateinit var mFileProvider: FileProvider
    private lateinit var mParams: ArumFilePicker.Params

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(activity!!).get(FilePickerViewModel::class.java)
        mDataBinding = FragmentFilepickerBinding.inflate(inflater, container, false)
        mFileProvider = FileProvider(this.requireActivity(), this)
        mParams.genericFileExtensions?.let {
            mFileProvider.supportedFileExtensions = it
        }
        mParams.supportedMimeTypes?.let {
            mFileProvider.supportedFileMimeTypes = it
        }
        mFileProvider.registerLifeCycle(activity!!)
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
            val contentPageAdapter =
                ContentPageAdapter(it)
            val pages = ArrayList<ShowableFragment>()
            pages.add(
                ShowableFragment(
                    ContentType.PICTURE,
                    ContentPageFragment.getInstance(
                        ContentType.PICTURE
                    ),
                    "PictureFragment",
                    R.drawable.ic_image
                )
            )
            pages.add(
                ShowableFragment(
                    ContentType.VIDEO,
                    ContentPageFragment.getInstance(
                        ContentType.VIDEO
                    ),
                    "VideoFragment",
                    R.drawable.ic_video
                )
            )
            pages.add(
                ShowableFragment(
                    ContentType.AUDIO,
                    ContentPageFragment.getInstance(
                        ContentType.AUDIO
                    ),
                    "AudioFragment",
                    R.drawable.ic_music_circle
                )
            )
            pages.add(
                ShowableFragment(
                    ContentType.GENERIC_FILE,
                    ContentPageFragment.getInstance(
                        ContentType.GENERIC_FILE
                    ),
                    "DocumentFragment",
                    R.drawable.ic_file_document_box
                )
            )
            contentPageAdapter.addPages(pages)
            mDataBinding.vp2Pages.adapter = contentPageAdapter
            mDataBinding.vp2Pages.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){

                override fun onPageSelected(position: Int) {
                    mViewModel.currentContentType.value = pages[position].contentType
                }
            })
            mViewModel.currentContentType.value = pages[0].contentType
            val icons = pages.map {showableFragment->
                ResourcesCompat.getDrawable(resources, showableFragment.icon, activity?.theme)
            }
            mDataBinding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.setIconColor(R.color.tab_unselected_color)
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.setIconColor(R.color.tab_selected_color)
                }

            })
            mDataBinding
                .tabLayout
                .setSelectedTabIndicatorColor(ContextCompat
                    .getColor(activity!!, R.color.tab_selected_color))
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
                            mParams.listener.onFilesReadyListener(safeSelectedList, safeContentType)
                            mViewModel.clearSelectedLists()
                            dismiss()
                        }
                    }
                })
            }
        })

        mViewModel.requestPermissionLiveData.observe(this, Observer {
            it?.use()?.let {
                mFileProvider.requestPermission()
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mViewModel.clearSelectedLists()
        mDataBinding.tabLayout.getTabAt(0)?.select()
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
            params: ArumFilePicker.Params
        ) = FilePickerBottomSheet().apply {
            mParams = params
        }
    }
}
