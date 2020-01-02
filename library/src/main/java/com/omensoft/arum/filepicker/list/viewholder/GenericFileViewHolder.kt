package com.omensoft.arum.filepicker.list.viewholder

import android.webkit.MimeTypeMap
import com.omensoft.arum.filepicker.databinding.ItemDocumentBinding
import com.omensoft.arum.filepicker.list.FileAdapter
import com.omensoft.arum.filepicker.model.GenericFile

class GenericFileViewHolder(viewDataBinding: ItemDocumentBinding)
    : FileViewHolder<ItemDocumentBinding, GenericFile>(viewDataBinding) {

    override fun bind(file: GenericFile, onItemSelectListener: FileAdapter.OnItemSelected) {
        mDataBinding.cbSelectedItem.apply {
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
        mDataBinding.tvDescription.text = MimeTypeMap.getSingleton().getExtensionFromMimeType(file.mimeType)
        mDataBinding.clContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }
    }
}