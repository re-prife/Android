package com.mirim.refrigerator.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService

class IngredientMultiSelectAdapter (val context: Context?, val ingredients: List<Ingredient>, var selectedIngredient: ArrayList<Long>): BaseAdapter() {
    override fun getCount(): Int {
        return ingredients.size
    }

    override fun getItem(position: Int): Any {
        return ingredients[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val ingredient = ingredients[position]

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_ingredient_multi_select, null)

        val txtIngredientName = view.findViewById<TextView>(R.id.txt_ingredient_name_item)
        val imageIngredient = view.findViewById<ImageView>(R.id.image_ingredient_item)
        val imageSelected = view.findViewById<ImageView>(R.id.image_selected)

        txtIngredientName.text = ingredient.ingredientName
        txtIngredientName.isSelected = true
        //black, red, yellow, green
        val statusView = view.findViewById<View>(R.id.image_ingredient_status)
        statusView.setBackgroundTintList(context.getResources().getColorStateList(Ingredient.statusColor(ingredient.ingredientColor)))
        var imagePath = RetrofitService.IMAGE_BASE_URL+ingredient.ingredientImagePath
        Log.d("imagePath", imagePath)
        Glide.with(context).load(imagePath).into(imageIngredient);

        view.setOnClickListener {
            if(!selectedIngredient.contains(ingredient.ingredientId)) {
                selectedIngredient.add(ingredient.ingredientId)
                imageSelected.visibility = View.VISIBLE
            }
            else {
                selectedIngredient.remove(ingredient.ingredientId)
                imageSelected.visibility = View.INVISIBLE
            }
        }

        return view
    }
}