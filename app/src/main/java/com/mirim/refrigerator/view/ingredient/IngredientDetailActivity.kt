package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientDetailBinding
import com.mirim.refrigerator.dialog.IngredientDeleteDialog
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.DeleteIngredientsRequest
import com.mirim.refrigerator.server.responses.DeleteIngredientsResponse
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        binding.btnDelete.setOnClickListener {
            val dialog = IngredientDeleteDialog(ingredientName = ingredient?.ingredientName, listOf(DeleteIngredientsRequest(ingredient?.ingredientId)), applicationContext)
            dialog.show(supportFragmentManager, "")
        }

        binding.txtIngredientName.text = ingredient?.ingredientName
        binding.txtIngredientAmount.text = ingredient?.ingredientCount
        binding.txtIngredientBuyDate.text = ingredient?.ingredientPurchaseDate
        binding.txtIngredientExpiryDate.text = ingredient?.ingredientExpirationDate
        binding.txtIngredientCategory.text = Ingredient.typeKoreanConverter(ingredient?.ingredientCategory)
        binding.txtIngredientStore.text = Ingredient.storeKoreanConverter(ingredient?.ingredientSaveType)
        binding.txtIngredientMemo.text = ingredient?.ingredientMemo
        Glide
            .with(applicationContext)
            .load(RetrofitService.IMAGE_BASE_URL+ingredient?.ingredientImagePath)
            .error(R.drawable.placeholder)
            .fallback(R.drawable.placeholder)
            .into(binding.imageIngredient);

        binding.btnModify.setOnClickListener {
            val intent = Intent(applicationContext, IngredientModifyActivity::class.java)
            val b = Bundle()
            b.putParcelable("ingredient", ingredient)
            intent.putExtra("bundle", b)
            startActivity(intent)
            finish()
        }
    }



}