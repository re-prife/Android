package com.mirim.refrigerator.view.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityPasswordModifyBinding

class PasswordModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityPasswordModifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}