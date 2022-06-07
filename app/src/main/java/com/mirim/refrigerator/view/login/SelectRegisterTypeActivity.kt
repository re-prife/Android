package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mirim.refrigerator.databinding.ActivitySelectRegisterTypeBinding

class SelectRegisterTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectRegisterTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectRegisterTypeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        // TODO
        Log.d("TAG-SelectRegister", "-SelectRegisterTypeActivity-")


        binding.btnCreateGroup.setOnClickListener {
            val intent = Intent(applicationContext, InputGroupNameActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.btnJoinGroup.setOnClickListener {
            val intent = Intent(applicationContext,InputGroupCodeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }


}