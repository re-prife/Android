package com.mirim.refrigerator.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.view.ingredient.IngredientDetailActivity

class IngredientSearchAdapter(val context: Context, val list: List<Ingredient>) : RecyclerView.Adapter<IngredientSearchAdapter.ViewHolder>() {
    class ViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {
        val txtIngredientCategory = view.findViewById<TextView>(R.id.txt_ingredient_search)
        val txtIngredientName = view.findViewById<TextView>(R.id.txt_ingredient_name_search)
        val txtIngredientExpiryDate = view.findViewById<TextView>(R.id.txt_ingredient_expiry_date_search)
        val txtStatus = view.findViewById<View>(R.id.image_ingredient_status_search)
        val imageIngredient = view.findViewById<ImageView>(R.id.image_ingredient_search)

        fun bind(ingredient: Ingredient) {
            txtIngredientCategory.text = Ingredient.storeKoreanConverter(ingredient.ingredientSaveType)+"/"+Ingredient.typeKoreanConverter(ingredient.ingredientCategory)
            txtIngredientName.text = ingredient.ingredientName
            txtIngredientExpiryDate.text = "유통기한 : " + ingredient.ingredientExpirationDate
            txtStatus.setBackgroundTintList(context.getResources().getColorStateList(Ingredient.statusColor(ingredient.ingredientColor)))
            Glide
                .with(context)
                .load(RetrofitService.IMAGE_BASE_URL+ingredient.ingredientImagePath)
                .error(R.drawable.placeholder)
                .fallback(R.drawable.placeholder)
                .into(imageIngredient);
            view.setOnClickListener {
                val intent = Intent(context, IngredientDetailActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                var b = Bundle()
                b.putParcelable("ingredient", ingredient)
                intent.putExtra("bundle", b)
                context?.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ingredient_search_recyclerview, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}