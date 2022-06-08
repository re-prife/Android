package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientDetailBinding
import com.mirim.refrigerator.model.Ingredient

class IngredientDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityIngredientDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ingredient: Ingredient? = intent.getBundleExtra("bundle")?.getParcelable("ingredient")

        binding.toolbar.toolbarTitle.text = "식재료 세부"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.txtIngredientName.text = ingredient?.ingredientName
        binding.txtIngredientAmount.text = ingredient?.ingredientCount
        binding.txtIngredientBuyDate.text = ingredient?.ingredientPurchaseDate
        // binding.txtIngredientExpiryDate.text = ingredient?.ingredientExpirationDate?.joinToString("-")
        binding.txtIngredientCategory.text = Ingredient.typeKoreanConverter(ingredient?.ingredientCategory)
        binding.txtIngredientStore.text = Ingredient.storeKoreanConverter(ingredient?.ingredientSaveType)
        binding.txtIngredientMemo.text = ingredient?.ingredientMemo

        binding.btnModify.setOnClickListener {
            val intent = Intent(applicationContext, IngredientModifyActivity::class.java)
            val b = Bundle()
            b.putParcelable("ingredient", ingredient)
            intent.putExtra("bundle", b)
            startActivity(intent)

        }



    }
}