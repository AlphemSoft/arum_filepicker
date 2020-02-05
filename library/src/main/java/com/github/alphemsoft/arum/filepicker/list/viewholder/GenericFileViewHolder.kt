package com.github.alphemsoft.arum.filepicker.list.viewholder

import android.webkit.MimeTypeMap
import com.github.alphemsoft.arum.filepicker.R
import com.github.alphemsoft.arum.filepicker.databinding.ItemDocumentBinding
import com.github.alphemsoft.arum.filepicker.list.FileAdapter
import com.github.alphemsoft.arum.filepicker.model.GenericFile
import com.github.alphemsoft.arum.filepicker.util.CustomMimeTypeMap
import com.github.alphemsoft.arum.filepicker.util.extension.changeStateColorWithRes
import com.github.alphemsoft.arum.filepicker.util.isQVersionOrHigher
import java.io.File

class GenericFileViewHolder(viewDataBinding: ItemDocumentBinding)
    : FileViewHolder<ItemDocumentBinding, GenericFile>(viewDataBinding) {

    override fun bind(file: GenericFile, onItemSelectListener: FileAdapter.OnItemSelected) {
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
        if (!isQVersionOrHigher()){
            mDataBinding.tvDescription.text =
                file.physicalPath?.let {
                    val f = File(it)
                    f.extension
                }
        }else{
            val mimeTypeMap = MimeTypeMap.getSingleton()
            if (mimeTypeMap.hasMimeType(file.mimeType?:"")){
                mDataBinding.tvDescription.text =
                    mimeTypeMap.getExtensionFromMimeType(file.mimeType)
            }else if (CustomMimeTypeMap.hasMimeType(file.mimeType)){
                mDataBinding.tvDescription.text =
                    CustomMimeTypeMap.getExtensionFromMimeType(file.mimeType!!)
            }
        }

        mDataBinding.clContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }
    }
}