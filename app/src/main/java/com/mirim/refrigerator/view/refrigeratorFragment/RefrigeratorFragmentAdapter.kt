package com.mirim.refrigerator.view.refrigeratorFragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RefrigeratorFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return Fragment1()
            1 -> return Fragment2()
            2 -> return Fragment3()
            3 -> return Fragment4()

        }
        return Fragment()
    }
}