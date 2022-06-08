package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.databinding.ActivityIngredientRegisterBinding
import com.mirim.refrigerator.view.BottomAppBarActivity

class IngredientRegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityIngredientRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientRegisterBinding.inflate(layoutInflater)
        val view = binding.root


        binding.toolbar.btnBack.setOnClickListener {
           finish()
        }
        binding.toolbar.toolbarTitle.text = "식재료 등록"
        binding.btnSaveIngredient.setOnClickListener {
            // TODO : 식재료 저장
        }
        binding.btnCancelIngredient.setOnClickListener {
            var intent = Intent(applicationContext, BottomAppBarActivity::class.java)
            intent.putExtra("clicked button", "refrigerator")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


        setContentView(view)
    }
}