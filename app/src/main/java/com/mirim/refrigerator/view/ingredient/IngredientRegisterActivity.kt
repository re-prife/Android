package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientRegisterBinding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.CreateIngredientRequest
import com.mirim.refrigerator.server.responses.CreateGroupResponse
import com.mirim.refrigerator.server.responses.CreateIngredientResponse
import com.mirim.refrigerator.view.BottomAppBarActivity
import com.mirim.refrigerator.viewmodel.app
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientRegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityIngredientRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        val categoryAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.ingredient_category, android.R.layout.simple_spinner_item)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        val saveTypeAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.ingredient_saveType, android.R.layout.simple_spinner_item)
        saveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKeepType.adapter = saveTypeAdapter

        binding.toolbar.btnBack.setOnClickListener {
           finish()
        }
        binding.toolbar.toolbarTitle.text = "식재료 등록"
        binding.btnSaveIngredient.setOnClickListener {
            var ingredientName = binding.editName.text.toString()
            var ingredientAmount = binding.editAmount.text.toString()
            var ingredientPurchaseDate = binding.editBoughtDay.text.toString()
            var ingredientExpirationDate = binding.editEndDay.text.toString()
            var ingredientCategory = Ingredient.typeEnglishConverter(binding.spinnerCategory.selectedItem.toString())
            var ingredientKeepType = Ingredient.storeEnglishConverter(binding.spinnerKeepType.selectedItem.toString())
            var ingredientMemo = binding.editMemo.text.toString()

            createIngredient(CreateIngredientRequest(ingredientName = ingredientName, ingredientCount = ingredientAmount, ingredientPurchaseDate = ingredientPurchaseDate, ingredientExpirationDate = ingredientExpirationDate,
            ingredientCategory = ingredientCategory, ingredientSaveType = ingredientKeepType, ingredientMemo = ingredientMemo))
        }
        binding.btnCancelIngredient.setOnClickListener {
            finish()
        }

        setContentView(view)
    }

    fun createIngredient(data: CreateIngredientRequest) {
        RetrofitService.ingredientAPI.createIngredients(app.user.groupId, data).enqueue(object : Callback<CreateIngredientResponse> {
            override fun onResponse(
                call: Call<CreateIngredientResponse>,
                response: Response<CreateIngredientResponse>
            ) {
                Log.d("IngredientRegisterActivity-createIngredient", response.toString())
                Toast.makeText(applicationContext, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onFailure(call: Call<CreateIngredientResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "실패하였습니다.", Toast.LENGTH_SHORT).show()
            }

        })

    }
}


