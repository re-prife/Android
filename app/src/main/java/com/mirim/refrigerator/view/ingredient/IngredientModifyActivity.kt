package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientModifyBinding

class IngredientModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityIngredientModifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.toolbarTitle.text = "식재료 수정"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

    }
}