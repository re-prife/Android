package com.mirim.refrigerator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService

class MyPageFamilyAdapter(val context: Context?, private val familyList : ArrayList<User>) :
    RecyclerView.Adapter<MyPageFamilyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView = itemView.findViewById(R.id.image_family_member)
        val nickname : TextView = itemView.findViewById(R.id.txt_family_member_nickname)
        val name : TextView = itemView.findViewById(R.id.txt_family_member_name)

        fun bind(item : User) {
            Glide.with(itemView)
                .load(RetrofitService.IMAGE_BASE_URL+item.userImagePath)
                .error(R.drawable.icon_profile)
                .fallback(R.drawable.icon_profile)
                .into(imageView)
            nickname.text = item.nickname
            name.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_family_member_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(familyList[position])
    }

    override fun getItemCount(): Int = familyList.size

}