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
import com.mirim.refrigerator.databinding.FragmentRefrigerator4Binding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.IngredientsResponse
import com.mirim.refrigerator.viewmodel.app
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fragment4 : Fragment() {
    lateinit var binding: FragmentRefrigerator4Binding
    var ingredientMap = HashMap<String, ArrayList<Ingredient>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRefrigerator4Binding.inflate(inflater, container, false)
        val view = binding.root

        getIngredientRoomTemp()

        return view
    }

    fun getIngredientRoomTemp() {
        RetrofitService.serviceAPI.getIngredients(app.user.groupId, "ROOM_TEMP")
            .enqueue(object : Callback<List<IngredientsResponse>> {
                override fun onResponse(
                    call: Call<List<IngredientsResponse>>,
                    response: Response<List<IngredientsResponse>>
                ) {
                    if(response.isSuccessful && response.body() != null) {
                        Log.d("Fragment2", "성공")
                        Log.d("Fragment2", response.body().toString())
                        for(ingredient in response.body()!!) {
                            Log.d("Fragment2", ingredient.toString())

                            val item = Ingredient(ingredient.ingredientCategory, ingredient.ingredientCount, ingredient.ingredientExpirationDate, "",
                                ingredient.ingredientName, "", ingredient.ingredientSaveType, ingredient.ingredientImageName)
                            if(ingredientMap.contains(ingredient.ingredientCategory)) {
                                ingredientMap.get(ingredient.ingredientCategory)?.add(item)
                            }
                            else {
                                ingredientMap.set(ingredient.ingredientCategory, arrayListOf<Ingredient>(item))
                            }
                        }

                        Log.d("Fragment2", ingredientMap.toString())

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