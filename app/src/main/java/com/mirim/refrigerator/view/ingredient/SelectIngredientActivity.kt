package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.adapter.IngredientGridAdapter
import com.mirim.refrigerator.adapter.IngredientMultiSelectAdapter
import com.mirim.refrigerator.databinding.ActivitySelectIngredientBinding
import com.mirim.refrigerator.dialog.IngredientDeleteDialog
import com.mirim.refrigerator.dialog.IngredientModifyDialog
import com.mirim.refrigerator.dialog.ShowCodeDialog
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectIngredientActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectIngredientBinding
    lateinit var freezerIngredient: List<Ingredient>
    lateinit var fridgeIngredient: List<Ingredient>
    lateinit var roomTempIngredient: List<Ingredient>
    public var selectedIngredients = ArrayList<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectIngredientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.toolbar.toolbarTitle.text = "식재료 관리"

        getIngredientFreezer()
        getIngredientFrigde()
        getIngredientRoomTemp()

        binding.btnModify.setOnClickListener {
            Toast.makeText(applicationContext, "수정", Toast.LENGTH_SHORT).show()
            val dialog = IngredientModifyDialog(emptyList(), emptyList())
            dialog.show(supportFragmentManager, "")

        }

        binding.btnDelete.setOnClickListener {
            Toast.makeText(applicationContext, "삭제", Toast.LENGTH_SHORT).show()
            val dialog = IngredientDeleteDialog("팽이버섯")
            dialog.show(supportFragmentManager, "")
        }
    }

    fun getIngredientFreezer() {
        RetrofitService.ingredientAPI.getIngredients(App.user.groupId, "FREEZER").enqueue(object : Callback<List<Ingredient>> {
            override fun onResponse(
                call: Call<List<Ingredient>>,
                response: Response<List<Ingredient>>
            ) {
                freezerIngredient = response.body()!!
                binding.gridFreezer.adapter = IngredientMultiSelectAdapter(applicationContext, freezerIngredient, selectedIngredients)
                binding.gridFreezer.setExpand(true)
            }

            override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                Log.d("SelectIngredientActivity", t.toString())
            }

        })
    }

    fun getIngredientFrigde() {
        RetrofitService.ingredientAPI.getIngredients(App.user.groupId, "FRIDGE").enqueue(object : Callback<List<Ingredient>> {
            override fun onResponse(
                call: Call<List<Ingredient>>,
                response: Response<List<Ingredient>>
            ) {
                fridgeIngredient = response.body()!!
                binding.gridFridge.adapter = IngredientMultiSelectAdapter(applicationContext, fridgeIngredient, selectedIngredients)
                binding.gridFridge.setExpand(true)
            }

            override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                Log.d("SelectIngredientActivity", t.toString())
            }

        })
    }
    fun getIngredientRoomTemp() {
        RetrofitService.ingredientAPI.getIngredients(App.user.groupId, "ROOM_TEMP").enqueue(object : Callback<List<Ingredient>> {
            override fun onResponse(
                call: Call<List<Ingredient>>,
                response: Response<List<Ingredient>>
            ) {
                roomTempIngredient = response.body()!!
                binding.gridRoomTemp.adapter = IngredientMultiSelectAdapter(applicationContext, roomTempIngredient, selectedIngredients)
                binding.gridRoomTemp.setExpand(true)
            }

            override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                Log.d("SelectIngredientActivity", t.toString())
            }

        })
    }
}