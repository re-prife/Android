package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientModifyBinding
import com.mirim.refrigerator.model.Ingredient

class IngredientModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityIngredientModifyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ingredient = intent.getParcelableExtra<Ingredient>("ingredient")

        binding.editName.setText(ingredient?.ingredientName)
        binding.editAmount.setText(ingredient?.ingredientCount)
        binding.editBoughtDay.setText(ingredient?.ingredientPurchaseDate)
        //binding.editEndDay.setText(ingredient?.ingredientExpirationDate)
        binding.editCategory.setText(Ingredient.typeKoreanConverter(ingredient?.ingredientCategory))
        binding.editKeepType.setText(Ingredient.storeKoreanConverter(ingredient?.ingredientSaveType))
        binding.editMemo.setText(ingredient?.ingredientMemo)

        binding.toolbar.toolbarTitle.text = "식재료 수정"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSaveIngredient.setOnClickListener {

        }

        binding.btnCancelIngredient.setOnClickListener {
            finish()
        }

    }
}