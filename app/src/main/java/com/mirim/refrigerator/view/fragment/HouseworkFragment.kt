package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.FragmentHouseworkBinding
import com.mirim.refrigerator.databinding.FragmentMyPageBinding
import com.mirim.refrigerator.dialog.HouseworkDetailDialog
import com.mirim.refrigerator.dialog.ShowCodeDialog
import com.mirim.refrigerator.view.housework.RegisterHouseworkActivity
import com.mirim.refrigerator.view.login.SignupActivity

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

        binding.btnHouseworkDetail.setOnClickListener {
            val dialog = HouseworkDetailDialog("카테고리", "설거지", "아빠", "2022.05.29", "2022.05.27", "2022.05.28")
            dialog.show(requireActivity().supportFragmentManager, "")
        }

        binding.btnAddHousework.setOnClickListener {
            var intent = Intent(context, RegisterHouseworkActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        return view
    }
}