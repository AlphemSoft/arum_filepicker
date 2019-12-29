package com.omensoft.arum.filepicker.paging

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

internal class ContentPageAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle) {
    private val pages:MutableList<ShowableFragment> =  ArrayList()

    fun addPages(newPages: List<ShowableFragment>){
        pages.clear()
        pages.addAll(newPages)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position].fragment
    }

}
