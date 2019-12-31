package com.omensoft.arum.filepicker.list.viewholder

import android.view.View
import com.omensoft.arum.filepicker.databinding.ItemVideoBinding
import com.omensoft.arum.filepicker.list.FileAdapter
import com.omensoft.arum.filepicker.model.Video

class VideoAdapter(dataBinding: ItemVideoBinding): FileViewHolder<ItemVideoBinding, Video>(dataBinding) {

    override fun bind(file: Video, onItemSelectListener: FileAdapter.OnItemSelected) {
        mDataBinding.clContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }

        mDataBinding.tvIndexIndicator.visibility = if (file.selected) View.VISIBLE else View.GONE

    }
}