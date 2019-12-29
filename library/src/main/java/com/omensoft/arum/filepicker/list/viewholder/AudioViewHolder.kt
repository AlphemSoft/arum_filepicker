package com.omensoft.arum.filepicker.list.viewholder

import com.omensoft.arum.filepicker.databinding.ItemAudioBinding
import com.omensoft.arum.filepicker.list.FileAdapter
import com.omensoft.arum.filepicker.model.Audio

class AudioViewHolder(viewDataBinding: ItemAudioBinding)
    : FileViewHolder<ItemAudioBinding, Audio>(viewDataBinding) {

    override fun bind(file: Audio, onItemSelectListener: FileAdapter.OnItemSelected) {
        mDataBinding.cbItemSelected.isSelected = file.selected
        mDataBinding.tvPosition.text = (adapterPosition+1).toString()
        mDataBinding.tvFileName.text = file.name
        mDataBinding.tvDescription.text = ""

        mDataBinding.cbItemSelected.setOnCheckedChangeListener { _, _ ->
            onItemSelectListener.onItemSelected(file)
        }

        mDataBinding.clAudioContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }
    }

}