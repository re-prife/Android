package com.mirim.refrigerator.view.refrigeratorFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirim.refrigerator.adapter.IngredientTypeAdapter
import com.mirim.refrigerator.databinding.FragmentRefrigerator1Binding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.IngredientsResponse
import com.mirim.refrigerator.view.ingredient.IngredientDetailActivity
import com.mirim.refrigerator.viewmodel.app
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fragment1 : Fragment() {
    lateinit var binding: FragmentRefrigerator1Binding
    var ingredientMap = HashMap<String, ArrayList<Ingredient>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRefrigerator1Binding.inflate(inflater, container, false)
        val view = binding.root

        getIngredientAll();

        return view
    }

    fun getIngredientAll() {
        RetrofitService.serviceAPI.getIngredients(app.user.groupId)
            .enqueue(object : Callback<List<IngredientsResponse>> {
                override fun onResponse(
                    call: Call<List<IngredientsResponse>>,
                    response: Response<List<IngredientsResponse>>
                ) {
                    if(response.isSuccessful && response.body() != null) {
                        Log.d("Fragment1", "성공")
                        Log.d("Fragment1", response.body().toString())
                        for(ingredient in response.body()!!) {
                            Log.d("Fragment1", ingredient.toString())

                            val item = Ingredient(
                                ingredientCategory = ingredient.ingredientCategory,
                                ingredientCount = ingredient.ingredientCount,
                                ingredientExpirationDate = ingredient.ingredientExpirationDate,
                                ingredientMemo = ingredient.ingredientImageName,
                                ingredientName = ingredient.ingredientName,
                                ingredientPurchaseDate = ingredient.ingredientExpirationDate,
                                ingredientSaveType = ingredient.ingredientSaveType,
                                ingredientImageName = ingredient.ingredientImageName,
                            )
                            if(ingredientMap.contains(ingredient.ingredientCategory)) {
                                ingredientMap.get(ingredient.ingredientCategory)?.add(item)
                            }
                            else {
                                ingredientMap.set(ingredient.ingredientCategory!!, arrayListOf<Ingredient>(item))
                            }
                        }

                        Log.d("Fragment1", ingredientMap.toString())

                        binding.recyclerIngredientAll.layoutManager = LinearLayoutManager(context)
                        binding.recyclerIngredientAll.setHasFixedSize(false)

                        binding.recyclerIngredientAll.adapter = IngredientTypeAdapter(context, ingredientMap.keys.toList(), ingredientMap.values.toList())
                    }
                }

                override fun onFailure(call: Call<List<IngredientsResponse>>, t: Throwable) {
                    Log.d("Fragment1", "실패")
                }

            });
    }


}

