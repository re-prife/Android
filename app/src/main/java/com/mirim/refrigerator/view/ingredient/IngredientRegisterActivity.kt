package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.databinding.ActivityIngredientRegisterBinding

class IngredientRegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityIngredientRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }
}