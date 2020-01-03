package com.omensoft.arum.filepicker.list.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.omensoft.arum.filepicker.databinding.ItemPictureBinding
import com.omensoft.arum.filepicker.list.FileAdapter
import com.omensoft.arum.filepicker.model.Picture

class PictureViewHolder(itemPictureBinding: ItemPictureBinding)
    : FileViewHolder<ItemPictureBinding, Picture>(itemPictureBinding) {
    override fun bind(file: Picture, onItemSelectListener: FileAdapter.OnItemSelected) {
        if (file.selected){
            mDataBinding.tvIndexIndicator.text = file.selectedIndex.plus(1).toString()
            mDataBinding.tvIndexIndicator.visibility = View.VISIBLE
        }else{
            mDataBinding.tvIndexIndicator.visibility = View.GONE
        }

        Glide.with(mContext).load(file.uri).into(mDataBinding.ivContent)
        mDataBinding.clContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }
    }
}