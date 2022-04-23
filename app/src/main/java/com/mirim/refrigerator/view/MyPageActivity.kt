package com.mirim.refrigerator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mirim.refrigerator.databinding.ActivityMyPageBinding
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.viewmodel.UserViewModel

class MyPageActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    lateinit var binding: ActivityMyPageBinding

    companion object {
        val TAG = "태그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPageBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        userViewModel.loadUsers(User("아빠", "김아빠", "email@naver.com"))

        userViewModel.getUser().observe(this, Observer<User>{
            Log.d(TAG, "MainActivity - myNumberViewModel - currentValue 라이브 데이터 값 변경")
            binding.userNickname.text = it.nickname
            binding.userName.text = it.name
            binding.userEmail.text = it.email
        })



    }
}