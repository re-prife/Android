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


        // QR스캔 확인
        if(intent.getStringExtra("ingredientName") != null) {
            // TODO : Spinner 연결 (하진)
            val intent_saveType = intent.getStringExtra("ingredientSaveType")
            val intent_category = intent.getStringExtra("ingredientCategory")

            binding.editName.setText(intent.getStringExtra("ingredientName"))
            binding.editEndDay.setText(intent.getStringExtra("ingredientExpirationDate"))
            binding.editBoughtDay.setText(intent.getStringExtra("ingredientPurchaseDate"))
        }

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
            val ingredientName = binding.editName.text.toString()
            val ingredientAmount = binding.editAmount.text.toString()
            val ingredientPurchaseDate = binding.editBoughtDay.text.toString()
            val ingredientExpirationDate = binding.editEndDay.text.toString()
            val ingredientCategory = Ingredient.typeEnglishConverter(binding.spinnerCategory.selectedItem.toString())
            val ingredientKeepType = Ingredient.storeEnglishConverter(binding.spinnerKeepType.selectedItem.toString())
            val ingredientMemo = binding.editMemo.text.toString()

            createIngredient(CreateIngredientRequest(ingredientName = ingredientName, ingredientCount = ingredientAmount, ingredientPurchaseDate = ingredientPurchaseDate, ingredientExpirationDate = ingredientExpirationDate,
            ingredientCategory = ingredientCategory, ingredientSaveType = ingredientKeepType, ingredientMemo = ingredientMemo))
        }
        binding.btnCancelIngredient.setOnClickListener {
            finish()
        }

        setContentView(view)
    }

    private fun createIngredient(data: CreateIngredientRequest) {
        RetrofitService.serviceAPI.createIngredients(app.user.groupId, data).enqueue(object : Callback<CreateIngredientResponse> {
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


