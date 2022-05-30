package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.FragmentErrandBinding
import com.mirim.refrigerator.dialog.ErrandAllowedDialog
import com.mirim.refrigerator.dialog.ErrandArrivedDialog
import com.mirim.refrigerator.view.errand.CreateErrandActivity
import com.mirim.refrigerator.view.login.SignupActivity

class ErrandFragment: Fragment() {

    var _binding : FragmentErrandBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG = "태그"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentErrandBinding.inflate(inflater,container,false)
        val view = binding.root

        binding.btnAddErrand.setOnClickListener {
            var intent = Intent(view.context, CreateErrandActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.btnArrivedDialog.setOnClickListener {
            val dialog = ErrandArrivedDialog("심부름 제목", listOf("엄마","아빠"))
            dialog.show(requireActivity().supportFragmentManager,"")
        }
        binding.btnAllowedDialog.setOnClickListener {
            val dialog = ErrandAllowedDialog("심부름 제목", "아빠")
            dialog.show(requireActivity().supportFragmentManager,"")
        }



        return view
    }
}