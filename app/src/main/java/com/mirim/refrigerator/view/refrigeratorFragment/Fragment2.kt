package com.mirim.refrigerator.view.refrigeratorFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirim.refrigerator.R
import com.mirim.refrigerator.adapter.IngredientTypeAdapter
import com.mirim.refrigerator.databinding.FragmentRefrigerator1Binding
import com.mirim.refrigerator.databinding.FragmentRefrigerator2Binding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.IngredientsResponse
import com.mirim.refrigerator.viewmodel.app
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fragment2 : Fragment() {
    lateinit var binding: FragmentRefrigerator2Binding
    var ingredientMap = HashMap<String, ArrayList<Ingredient>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRefrigerator2Binding.inflate(inflater, container, false)
        val view = binding.root

        getIngredientFreezer()


        return view
    }
    fun getIngredientFreezer() {
        RetrofitService.serviceAPI.getIngredients(app.user.groupId, "FREEZER")
            .enqueue(object : Callback<List<Ingredient>> {
                override fun onResponse(
                    call: Call<List<Ingredient>>,
                    response: Response<List<Ingredient>>
                ) {
                    if(response.isSuccessful && response.body() != null) {
                        Log.d("Fragment2", "성공")
                        Log.d("Fragment2", response.body().toString())
                        for(ingredient in response.body()!!) {
                            Log.d("Fragment2", ""+ingredient.ingredientId)

                            val item = Ingredient(
                                ingredientCategory = ingredient.ingredientCategory,
                                ingredientCount = ingredient.ingredientCount,
                                ingredientExpirationDate = ingredient.ingredientExpirationDate,
                                ingredientMemo = ingredient.ingredientImageName,
                                ingredientName = ingredient.ingredientName,
                                ingredientPurchaseDate = ingredient.ingredientPurchaseDate,
                                ingredientSaveType = ingredient.ingredientSaveType,
                                ingredientImageName = ingredient.ingredientImageName,
                                ingredientId = ingredient.ingredientId!!,
                                ingredientColor = ingredient.ingredientColor
                            )
                            if(ingredientMap.contains(ingredient.ingredientCategory)) {
                                ingredientMap.get(ingredient.ingredientCategory)?.add(item)
                            }
                            else {
                                ingredientMap.set(ingredient.ingredientCategory!!, arrayListOf<Ingredient>(item))
                            }
                        }

                        Log.d("Fragment2", ingredientMap.toString())

                        binding.recyclerIngredientAll.layoutManager = LinearLayoutManager(context)
                        binding.recyclerIngredientAll.setHasFixedSize(false)

                        binding.recyclerIngredientAll.adapter = IngredientTypeAdapter(context, ingredientMap.keys.toList(), ingredientMap.values.toList())
                    }
                }

                override fun onFailure(call: Call<List<Ingredient>>, t: Throwable) {
                    Log.d("Fragment1", "실패")
                }

            });
    }

}