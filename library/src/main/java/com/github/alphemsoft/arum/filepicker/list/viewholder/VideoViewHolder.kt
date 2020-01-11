package com.github.alphemsoft.arum.filepicker.list.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.github.alphemsoft.arum.filepicker.databinding.ItemVideoBinding
import com.github.alphemsoft.arum.filepicker.list.FileAdapter
import com.github.alphemsoft.arum.filepicker.model.Video
import com.github.alphemsoft.arum.filepicker.util.Duration

class VideoViewHolder(dataBinding: ItemVideoBinding): FileViewHolder<ItemVideoBinding, Video>(dataBinding) {

    override fun bind(file: Video, onItemSelectListener: FileAdapter.OnItemSelected) {
        mDataBinding.clContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }

        mDataBinding.tvIndexIndicator.visibility = if (file.selected) View.VISIBLE else View.GONE
        mDataBinding.tvDuration.text = Duration(file.duration)
            .toString()
        mDataBinding.tvIndexIndicator.text = file.selectedIndex.plus(1).toString()
        Glide.with(mContext).load(file.uri).into(mDataBinding.ivContent)
    }
}