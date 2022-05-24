package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.databinding.ActivitySelectIngredientRegisterTypeBinding

class SelectIngredientRegisterType : AppCompatActivity() {

    private lateinit var binding: ActivitySelectIngredientRegisterTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectIngredientRegisterTypeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        binding.btnRegisterIngredient.setOnClickListener {
            val intent = Intent(applicationContext,IngredientRegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        binding.btnScanQr.setOnClickListener {
            val intent = Intent(applicationContext,QrScanActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


        /* Todo : 식재료 관리 탭 연결
        binding.btnMovePrev.setOnClickListener {
            var intent = Intent(applicationContext,식재료관리탭::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        } */
    }
}