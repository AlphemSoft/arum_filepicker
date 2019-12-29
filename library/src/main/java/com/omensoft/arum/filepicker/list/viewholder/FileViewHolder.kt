package com.omensoft.arum.filepicker.list.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.omensoft.arum.filepicker.list.FileAdapter
import com.omensoft.arum.filepicker.model.AbstractFile

abstract class FileViewHolder<DB: ViewDataBinding, in F: AbstractFile<*>> constructor(val mDataBinding: DB)
    :RecyclerView.ViewHolder(mDataBinding.root) {
    protected val mContext = mDataBinding.root.context
    abstract fun bind(file: F, onItemSelectListener: FileAdapter.OnItemSelected)
}
