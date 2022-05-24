package com.mirim.refrigerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.databinding.ActivityRefrigeratorSearchBinding

class RefrigeratorSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityRefrigeratorSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRefrigeratorSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }


    }
}