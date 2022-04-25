package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivitySelectRegisterTypeBinding
import com.mirim.refrigerator.view.ShowGroupCodeActivity

class SelectRegisterTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectRegisterTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectRegisterTypeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)


        binding.btnCreateGroup.setOnClickListener {
            var intent = Intent(applicationContext,ShowGroupCodeActivity::class.java)
            startActivity(intent)
        }
        binding.btnJoinGroup.setOnClickListener {
            var intent = Intent(applicationContext,InputGroupCodeActivity::class.java)
            startActivity(intent)
        }

    }
}