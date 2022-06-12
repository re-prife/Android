package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientModifyBinding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.CreateIngredientRequest
import com.mirim.refrigerator.server.request.DeleteIngredientsRequest
import com.mirim.refrigerator.server.responses.CreateIngredientResponse
import com.mirim.refrigerator.server.responses.DeleteIngredientsResponse
import com.mirim.refrigerator.server.responses.IngredientsResponse
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityIngredientModifyBinding
    lateinit var ingredient: Ingredient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.ingredient_category, android.R.layout.simple_spinner_item)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        val saveTypeAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.ingredient_saveType, android.R.layout.simple_spinner_item)
        saveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKeepType.adapter = saveTypeAdapter

        binding.spinnerCategory

        ingredient = intent.getBundleExtra("bundle")?.getParcelable("ingredient")!!

        binding.editName.setText(ingredient?.ingredientName)
        binding.editAmount.setText(ingredient?.ingredientCount)
        binding.editBoughtDay.setText(ingredient?.ingredientPurchaseDate)
        binding.editEndDay.setText(ingredient?.ingredientExpirationDate)
        binding.spinnerCategory.setSelection(Ingredient.categoryIndex(ingredient?.ingredientCategory))
        binding.spinnerKeepType.setSelection(Ingredient.storeIndex(ingredient?.ingredientSaveType))
        binding.editMemo.setText(ingredient?.ingredientMemo)
        Glide.with(applicationContext).load(RetrofitService.IMAGE_BASE_URL+ingredient?.ingredientImagePath).into(binding.imageIngredient);

        binding.toolbar.toolbarTitle.text = "식재료 수정"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSaveIngredient.setOnClickListener {
            val updatedIngredient = CreateIngredientRequest(
                ingredientCategory = Ingredient.typeEnglishConverter(binding.spinnerCategory.selectedItem.toString()),
                ingredientCount = binding.editAmount.text.toString(),
                ingredientExpirationDate = binding.editEndDay.text.toString(),
                ingredientMemo = binding.editMemo.text.toString(),
                ingredientName = binding.editName.text.toString(),
                ingredientPurchaseDate = binding.editBoughtDay.text.toString(),
                ingredientSaveType = Ingredient.storeEnglishConverter(binding.spinnerKeepType.selectedItem.toString()),
            )
            updateIngredient(updatedIngredient)

        }

        binding.btnCancelIngredient.setOnClickListener {
            Toast.makeText(applicationContext, "취소", Toast.LENGTH_SHORT).show()
        }

    }
    fun updateIngredient(body: CreateIngredientRequest) {
        RetrofitService.serviceAPI.updateIngredients(App.user.groupId, ingredient?.ingredientId, body).enqueue(object : Callback<CreateIngredientResponse> {
            override fun onResponse(
                call: Call<CreateIngredientResponse>,
                response: Response<CreateIngredientResponse>
            ) {
                Log.d("IngredientModifyAcitivity", response.toString())
                if(response.isSuccessful) {
                    if(response.code() == 204) {
                        Toast.makeText(applicationContext, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<CreateIngredientResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


}