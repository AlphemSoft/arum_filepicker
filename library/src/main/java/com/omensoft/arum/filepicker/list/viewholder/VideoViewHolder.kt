package com.omensoft.arum.filepicker.list.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.omensoft.arum.filepicker.databinding.ItemVideoBinding
import com.omensoft.arum.filepicker.list.FileAdapter
import com.omensoft.arum.filepicker.model.Video
import com.omensoft.arum.filepicker.util.Duration

class VideoViewHolder(dataBinding: ItemVideoBinding): FileViewHolder<ItemVideoBinding, Video>(dataBinding) {

    override fun bind(file: Video, onItemSelectListener: FileAdapter.OnItemSelected) {
        mDataBinding.clContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }

        mDataBinding.tvIndexIndicator.visibility = if (file.selected) View.VISIBLE else View.GONE
        mDataBinding.tvDuration.text = Duration(file.duration).toString()
        mDataBinding.tvIndexIndicator.text = file.selectedIndex.toString()
        Glide.with(mContext).load(file.uri).into(mDataBinding.ivContent)
    }
}