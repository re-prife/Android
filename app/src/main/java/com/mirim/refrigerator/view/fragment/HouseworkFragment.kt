package com.mirim.refrigerator.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.FragmentHouseworkBinding
import com.mirim.refrigerator.databinding.FragmentMyPageBinding

class HouseworkFragment: Fragment() {
    var _binding: FragmentHouseworkBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG = "태그"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseworkBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
}