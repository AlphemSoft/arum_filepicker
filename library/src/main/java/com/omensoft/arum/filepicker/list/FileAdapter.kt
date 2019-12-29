package com.omensoft.arum.filepicker.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omensoft.arum.filepicker.databinding.ItemAudioBinding
import com.omensoft.arum.filepicker.databinding.ItemPictureBinding
import com.omensoft.arum.filepicker.enums.ContentType
import com.omensoft.arum.filepicker.list.viewholder.AudioViewHolder
import com.omensoft.arum.filepicker.list.viewholder.FileViewHolder
import com.omensoft.arum.filepicker.list.viewholder.PictureViewHolder
import com.omensoft.arum.filepicker.model.AbstractFile

class FileAdapter(private val onItemSelected: OnItemSelected): RecyclerView.Adapter<FileViewHolder<*, *>>() {

    private val contentItems: MutableList<AbstractFile<*>> = ArrayList()

    fun addContent(newItems: List<AbstractFile<*>>){
        val diffUtil = DiffUtil.calculateDiff(object: DiffUtil.Callback(){
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return contentItems[oldItemPosition].id == newItems[newItemPosition].id
            }

            override fun getOldListSize(): Int = contentItems.size
            override fun getNewListSize(): Int = newItems.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return contentItems[oldItemPosition] == newItems[newItemPosition]
            }

        })

        contentItems.clear()
        contentItems.addAll(newItems)
        diffUtil.dispatchUpdatesTo(this)
    }

    @SuppressWarnings("unchecked")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder<*, *> {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding: ViewDataBinding
        return when(viewType){
            ContentType.AUDIO.type.hashCode() -> {
                dataBinding = ItemAudioBinding.inflate(inflater, parent, false)
                AudioViewHolder(dataBinding)
            }

            ContentType.PICTURE.type.hashCode()->{
                dataBinding = ItemPictureBinding.inflate(inflater, parent, false)
                PictureViewHolder(dataBinding)
            }

            else ->{
                dataBinding = ItemAudioBinding.inflate(inflater, parent, false)
                AudioViewHolder(dataBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return contentItems[position].contentType.type.hashCode()
    }

    override fun getItemCount(): Int = contentItems.size

    override fun onBindViewHolder(holder: FileViewHolder<*, *>, position: Int) {
        holder as FileViewHolder<ViewDataBinding, AbstractFile<*>>
        holder.bind(contentItems[holder.adapterPosition], onItemSelected)
    }

    interface OnItemSelected{
        fun onItemSelected(file: AbstractFile<*>)
    }
}