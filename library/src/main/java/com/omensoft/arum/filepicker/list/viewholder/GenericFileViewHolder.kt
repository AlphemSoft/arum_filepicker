package com.omensoft.arum.filepicker.list.viewholder

import com.omensoft.arum.filepicker.databinding.ItemDocumentBinding
import com.omensoft.arum.filepicker.list.FileAdapter
import com.omensoft.arum.filepicker.model.GenericFile

class DocumentViewHolder(viewDataBinding: ItemDocumentBinding)
    : FileViewHolder<ItemDocumentBinding, GenericFile>(viewDataBinding) {

    override fun bind(file: GenericFile, onItemSelectListener: FileAdapter.OnItemSelected) {
        mDataBinding.cbSelectedItem.isSelected = file.selected
        mDataBinding.tvPosition.text = (adapterPosition+1).toString()
        mDataBinding.tvFileName.text = file.name
        mDataBinding.tvDescription.text = file.extension

        mDataBinding.cbSelectedItem.setOnCheckedChangeListener { _, _ ->
            onItemSelectListener.onItemSelected(file)
        }

        mDataBinding.clContainer.setOnClickListener {
            onItemSelectListener.onItemSelected(file)
        }
    }

}