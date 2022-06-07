package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mirim.refrigerator.databinding.FragmentMyPageBinding
import com.mirim.refrigerator.dialog.ShowCodeDialog
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.view.mypage.PolicyActivity
import com.mirim.refrigerator.view.mypage.ProfileModifyActivity
import com.mirim.refrigerator.viewmodel.UserViewModel

class MyPageFragment: Fragment() {
    private lateinit var userViewModel: UserViewModel
    var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG = "태그"
        val currentVersion = "1.0.0"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val view = binding.root

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.userImage.clipToOutline = true

        userViewModel.loadUsers(User("아빠", "김아빠", "email@naver.com",1,1))

        userViewModel.getUser().observe(viewLifecycleOwner, Observer<User>{
            binding.userNickname.text = it.nickname
            binding.userName.text = it.name
            binding.userEmail.text = it.email
        })

        binding.btnGroupCode.setOnClickListener {
            val dialog = ShowCodeDialog("hajinhajin")
            dialog.show(requireActivity().supportFragmentManager, "")
        }

        binding.btnProfileSetting.setOnClickListener {
            startActivity(Intent(context, ProfileModifyActivity::class.java))
        }

        binding.btnVersion.setOnClickListener {
            Toast.makeText(context, "현재 버전 : ${currentVersion}", Toast.LENGTH_SHORT).show()
        }

        binding.btnPolicy.setOnClickListener {
            startActivity(Intent(context, PolicyActivity::class.java))
        }

        binding.btnContact.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("s2009@e-mirim.hs.kr"))
            startActivity(intent)
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}