package com.rafih.socialmediaapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rafih.socialmediaapp.fragment.host.FragmentOne
import com.rafih.socialmediaapp.fragment.host.FragmentTwo

class FragmentPagerAdapter(fm: FragmentManager, lc: Lifecycle): FragmentStateAdapter(fm,lc) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1 -> FragmentOne()
            else -> FragmentTwo()
        }
    }
}