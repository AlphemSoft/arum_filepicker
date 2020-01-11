package com.github.alphemsoft.arum.filepicker.list.viewholder

import com.github.alphemsoft.arum.filepicker.R
import com.github.alphemsoft.arum.filepicker.databinding.ItemAudioBinding
import com.github.alphemsoft.arum.filepicker.list.FileAdapter
import com.github.alphemsoft.arum.filepicker.model.Audio
import com.github.alphemsoft.arum.filepicker.util.extension.changeStateColorWithRes

class AudioViewHolder(viewDataBinding: ItemAudioBinding)
    : FileViewHolder<ItemAudioBinding, Audio>(viewDataBinding) {

    override fun bind(file: Audio, onItemSelectListener: FileAdapter.OnItemSelected){
        mDataBinding.cbSelectedItem.apply {
            changeStateColorWithRes(R.color.tab_state_selector)
            mDataBinding.cbSelectedItem.isChecked = false
            setOnClickListener {
                onItemSelectListener.onItemSelected(file)
            }
            post {
                if (file.selected && !mDataBinding.cbSelectedItem.isChecked){
                    mDataBinding.cbSelectedItem.toggle()
                }
            }
        }
        mDataBinding.tvPosition.text = (adapterPosition+1).toString()
        mDataBinding.tvFileName.text = file.name
        mDataBinding.tvDescription.text = "AUDIO"
        mDataBinding.clContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }
    }

}