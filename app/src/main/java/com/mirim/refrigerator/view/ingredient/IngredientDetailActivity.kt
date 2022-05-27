package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientDetailBinding

class IngredientDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityIngredientDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.toolbarTitle.text = "식재료 세부"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.btnModify.setOnClickListener {
            startActivity(Intent(applicationContext, IngredientModifyActivity::class.java))

        }



    }
}