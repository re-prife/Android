package com.mirim.refrigerator.view.refrigeratorFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirim.refrigerator.databinding.FragmentRefrigerator1Binding
import com.mirim.refrigerator.view.ingredient.IngredientDetailActivity

class Fragment1 : Fragment() {
    lateinit var binding: FragmentRefrigerator1Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRefrigerator1Binding.inflate(inflater, container, false)
        val view = binding.root

        binding.modify.setOnClickListener {
            startActivity(Intent(context, IngredientDetailActivity::class.java))

        }
        return view
    }
}