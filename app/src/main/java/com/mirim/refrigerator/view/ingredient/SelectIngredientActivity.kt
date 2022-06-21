package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.mirim.refrigerator.server.request.DeleteIngredientsRequest
import com.mirim.refrigerator.server.responses.DeleteIngredientsResponse
import com.mirim.refrigerator.viewmodel.App
import kotlinx.coroutines.selects.select
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectIngredientActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectIngredientBinding
    lateinit var freezerIngredient: List<Ingredient>
    lateinit var fridgeIngredient: List<Ingredient>
    lateinit var roomTempIngredient: List<Ingredient>
    public var selectedIngredients = ArrayList<Ingredient>()

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
            if(selectedIngredients.isEmpty()) {
                Toast.makeText(applicationContext, "수정할 식재료를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                val dialog = IngredientModifyDialog(selectedIngredients, applicationContext)
                dialog.show(supportFragmentManager, "")
            }


        }

        binding.btnDelete.setOnClickListener {
            if(selectedIngredients.isEmpty()) {
                Toast.makeText(applicationContext, "삭제할 식재료를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                val list= ArrayList<DeleteIngredientsRequest>()
                for(selected in selectedIngredients) {
                    list.add(DeleteIngredientsRequest(selected.ingredientId))
                }
                val dialog = IngredientDeleteDialog(ingredientName = "선택한 식재료 모두", list, this)
                dialog.show(supportFragmentManager, "")
            }

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
                if(freezerIngredient.isEmpty()) {
                    binding.txtFreezer.visibility = View.GONE
                }
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
                if(fridgeIngredient.isEmpty()) {
                    binding.txtFridge.visibility = View.GONE
                }
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
                if(roomTempIngredient.isEmpty()) {
                    binding.txtRoomTemp.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                Log.d("SelectIngredientActivity", t.toString())
            }

        })
    }

    override fun onResume() {
        super.onResume()
        getIngredientFreezer()
        getIngredientFrigde()
        getIngredientRoomTemp()
    }
}