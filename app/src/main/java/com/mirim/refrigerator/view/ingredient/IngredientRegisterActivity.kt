package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mirim.refrigerator.databinding.ActivityIngredientRegisterBinding
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

        binding.toolbar.btnBack.setOnClickListener {
           finish()
        }
        binding.toolbar.toolbarTitle.text = "식재료 등록"
        binding.btnSaveIngredient.setOnClickListener {
            var ingredientName = binding.editName.text.toString()
            var ingredientAmount = binding.editAmount.text.toString()
            var ingredientPurchaseDate = binding.editBoughtDay.text.toString()
            var ingredientExpirationDate = binding.editEndDay.text.toString()
            var ingredientCategory = binding.editCategory.text.toString()
            var ingredientKeepType = binding.editKeepType.text.toString()
            var ingredientMemo = binding.editMemo.text.toString()

            createIngredient(CreateIngredientRequest(ingredientName, ingredientAmount, ingredientPurchaseDate, ingredientExpirationDate,
            ingredientCategory, ingredientKeepType, ingredientMemo))
        }
        binding.btnCancelIngredient.setOnClickListener {
            var intent = Intent(applicationContext, BottomAppBarActivity::class.java)
            intent.putExtra("clicked button", "refrigerator")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        setContentView(view)
    }
    fun createIngredient(data: CreateIngredientRequest) {
        RetrofitService.serviceAPI.createIngredients(app.user.groupId, data).enqueue(object : Callback<CreateIngredientResponse> {
            override fun onResponse(
                call: Call<CreateIngredientResponse>,
                response: Response<CreateIngredientResponse>
            ) {
                var status = response.raw().code()
                Log.d("IngredientRegisterActivity", ""+status)
                Log.d("IngredientRegisterActivity", response.toString())
            }

            override fun onFailure(call: Call<CreateIngredientResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
}


