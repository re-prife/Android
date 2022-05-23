package com.mirim.refrigerator.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.FragmentRefrigeratorBinding
import com.mirim.refrigerator.view.refrigeratorFragment.Fragment1
import com.mirim.refrigerator.view.refrigeratorFragment.Fragment2
import com.mirim.refrigerator.view.refrigeratorFragment.Fragment3
import com.mirim.refrigerator.view.refrigeratorFragment.Fragment4


class RefrigeratorFragment: Fragment() {
    var _binding: FragmentRefrigeratorBinding? = null
    private val binding get() = _binding!!

    val fragment1 = Fragment1()
    val fragment2 = Fragment2()
    val fragment3 = Fragment3()
    val fragment4 = Fragment4()

    companion object {
        val TAG = "태그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRefrigeratorBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.myToolbar.inflateMenu(R.menu.refrigerator_menu)

        val tabLayout = binding.tabLayout
        childFragmentManager.beginTransaction().replace(R.id.frame, fragment1).commit()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                var selected: Fragment? = null
                if (position == 0) {
                    selected = fragment1
                } else if (position == 1) {
                    selected = fragment2
                } else if (position == 2) {
                    selected = fragment3
                } else if (position == 3) {
                    selected = fragment4
                }
                if (selected != null) {
                    childFragmentManager.beginTransaction().replace(R.id.frame, selected)
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}


        })

        binding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    // Navigate to settings screen
                    true
                }
                R.id.action_select -> {
                    // Save profile changes
                    true
                }
                else -> false
            }
        }

        return view
    }
}

