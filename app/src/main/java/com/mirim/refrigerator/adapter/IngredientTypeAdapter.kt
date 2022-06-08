package com.mirim.refrigerator.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.view.ingredient.IngredientDetailActivity

class IngredientTypeAdapter(val context: Context?, val ingredientTypes: List<String>, val ingredientList: List<List<Ingredient>>) : RecyclerView.Adapter<IngredientTypeAdapter.ViewHolder>() {

    public class ViewHolder(val itemView: View, val context: Context?) : RecyclerView.ViewHolder(itemView) {
        val txtIngredientType = itemView.findViewById<TextView>(R.id.txt_ingredient_type)
        val gridIngredient = itemView.findViewById<GridView>(R.id.grid_ingredient)

        fun bind(ingredientType: String, ingredients: List<Ingredient>) {
            txtIngredientType.text = Ingredient.typeKoreanConverter(ingredientType)
            gridIngredient.adapter = IngredientGridAdapter(context, ingredients)

            gridIngredient.setOnItemClickListener { parent, view, position, id ->
                val intent = Intent(context, IngredientDetailActivity::class.java)
                intent.putExtra("ingredient", ingredients[position])
                context?.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ingredient_type_recycler, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredientTypes[position], ingredientList[position])
    }

    override fun getItemCount(): Int {
        return ingredientTypes.size
    }
}