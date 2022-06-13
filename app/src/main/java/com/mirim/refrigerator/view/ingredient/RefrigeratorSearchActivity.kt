package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirim.refrigerator.adapter.IngredientSearchAdapter
import com.mirim.refrigerator.adapter.IngredientTypeAdapter
import com.mirim.refrigerator.databinding.ActivityRefrigeratorSearchBinding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.viewmodel.app
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefrigeratorSearchActivity : AppCompatActivity() {
    lateinit var binding: ActivityRefrigeratorSearchBinding
    lateinit var ingredientList: List<Ingredient>
    lateinit var searchList: MutableList<Ingredient>
    lateinit var adapter: IngredientSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRefrigeratorSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchList = mutableListOf()

        getIngredientAll();

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val text = binding.editSearch.text.toString()
                search(text)
            }

        })


        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    fun search(text: String) {
        searchList.clear()
        if(text.isNotBlank()) {
            for(ingredient in ingredientList) {
                if(ingredient.ingredientName!!.contains(text)) {
                    searchList.add(ingredient)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun getIngredientAll() {
        RetrofitService.ingredientAPI.getIngredients(app.user.groupId)
            .enqueue(object : Callback<List<Ingredient>> {
                override fun onResponse(
                    call: Call<List<Ingredient>>,
                    response: Response<List<Ingredient>>
                ) {
                    if(response.isSuccessful && response.body() != null) {
                        ingredientList = response.body()!!

                        binding.recyclerIngredientSearch.layoutManager = LinearLayoutManager(applicationContext)
                        binding.recyclerIngredientSearch.setHasFixedSize(false)
                        adapter = IngredientSearchAdapter(applicationContext, searchList)
                        binding.recyclerIngredientSearch.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                    Log.d("Fragment1", "실패")
                }

            });
    }
}