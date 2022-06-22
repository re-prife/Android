package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mirim.refrigerator.R
import com.mirim.refrigerator.view.ingredient.RefrigeratorSearchActivity
import com.mirim.refrigerator.databinding.FragmentRefrigeratorBinding
import com.mirim.refrigerator.view.ingredient.SelectIngredientActivity
import com.mirim.refrigerator.view.ingredient.SelectIngredientRegisterTypeActivity
import com.mirim.refrigerator.view.refrigeratorFragment.*


class RefrigeratorFragment: Fragment() {
    var _binding: FragmentRefrigeratorBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG = "태그"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRefrigeratorBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.myToolbar.inflateMenu(R.menu.refrigerator_menu)
        setHasOptionsMenu(true)

        val tabLayout = binding.tabLayout

        val refrigeratorFragmentAdapter = RefrigeratorFragmentAdapter(this)
        val viewPager = binding.pager
        viewPager.adapter = refrigeratorFragmentAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "전체"
                1 -> tab.text = "냉동"
                2 -> tab.text = "냉장"
                3 -> tab.text = "실온"
            }
        }.attach()

        binding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    startActivity(Intent(context, RefrigeratorSearchActivity::class.java))
                    true
                }
                R.id.action_select -> {
                    var intent = Intent(context, SelectIngredientActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        binding.btnPlus.setOnClickListener {
            val intent = Intent(context, SelectIngredientRegisterTypeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        return view
    }
}

