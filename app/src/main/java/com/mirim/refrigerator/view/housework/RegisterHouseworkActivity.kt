package com.mirim.refrigerator.view.housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityRegisterHouseworkBinding

class RegisterHouseworkActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterHouseworkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterHouseworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnSaveIngredient.setOnClickListener {
 //
 //       }
   //     binding.btnCancelIngredient.setOnClickListener {
  //
    //    }
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.toolbar.toolbarTitle.text = "집안일 정하기"
    }
}