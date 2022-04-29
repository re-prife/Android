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
            var intent = Intent(applicationContext,IngredientRegisterActivity::class.java)
            startActivity(intent)
        }
    }
}