package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.databinding.ActivityIngredientRegisterBinding

class IngredientRegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityIngredientRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientRegisterBinding.inflate(layoutInflater)
        val view = binding.root


        binding.btnMovePrev.setOnClickListener {
            var intent = Intent(applicationContext,SelectIngredientRegisterType::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        binding.btnSaveIngredient.setOnClickListener {
            // TODO : 식재료 저장
        }
        binding.btnCancelIngredient.setOnClickListener {
            // TODO : 식재료 관리 탭 연결
        }


        setContentView(view)
    }
}