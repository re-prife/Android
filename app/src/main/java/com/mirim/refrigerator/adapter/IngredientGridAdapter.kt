package com.mirim.refrigerator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.Ingredient
import java.util.zip.Inflater

class IngredientGridAdapter(val context: Context?, val ingredients: List<Ingredient>): BaseAdapter() {
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
        val view = inflater.inflate(R.layout.item_ingredient_recyclerview, null)

        view.findViewById<TextView>(R.id.txt_ingredient_name_item).text = ingredient.ingredientName
        //TODO() : "이미지 연결"
        //TODO("유통기한 상태 파악")

        return view
    }
}