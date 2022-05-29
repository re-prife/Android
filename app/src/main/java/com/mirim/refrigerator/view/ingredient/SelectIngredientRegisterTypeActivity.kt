package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.databinding.ActivitySelectIngredientRegisterTypeBinding

class SelectIngredientRegisterTypeActivity : AppCompatActivity() {

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

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }
}