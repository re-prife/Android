package com.mirim.refrigerator.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mirim.refrigerator.databinding.FragmentMyPageBinding
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.viewmodel.UserViewModel

class MyPageFragment: Fragment() {
    private lateinit var userViewModel: UserViewModel
    var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG = "태그"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.userImage.clipToOutline = true

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        userViewModel.loadUsers(User("아빠", "김아빠", "email@naver.com"))

        userViewModel.getUser().observe(this, Observer<User>{
            binding.userNickname.text = it.nickname
            binding.userName.text = it.name
            binding.userEmail.text = it.email
        })


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}