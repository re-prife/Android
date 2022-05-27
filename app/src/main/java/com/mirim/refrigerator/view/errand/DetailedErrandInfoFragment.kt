package com.mirim.refrigerator.view.errand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityDetailedErrandInfoBinding

class DetailedErrandInfoFragment : Fragment() {
    lateinit var binding: ActivityDetailedErrandInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityDetailedErrandInfoBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.toolbar.toolbarTitle.text = "심부름 세부"
        binding.toolbar.btnBack.setOnClickListener {

        }

        return view
    }
}
