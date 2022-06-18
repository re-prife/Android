package com.mirim.refrigerator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService

class MakeErrandFamilyAdapter (val context: Context?, private val familyList : ArrayList<FamilyMember>) :
    RecyclerView.Adapter<MakeErrandFamilyAdapter.ViewHolder>() {

    companion object {
        lateinit var selectedMemberList : ArrayList<Int>
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profileImage : ImageView = itemView.findViewById(R.id.img_profile)
        val nickname : TextView = itemView.findViewById(R.id.txt_nickname)
        private val selectedProfile : ImageView = itemView.findViewById(R.id.img_selected_profile)
        private val selectedIcon : ImageView = itemView.findViewById(R.id.icon_selected)


        fun bind(item : FamilyMember) {
            profileImage.clipToOutline = true
            Glide.with(itemView)
                .load(RetrofitService.IMAGE_BASE_URL+item.userImagePath)
                .error(R.drawable.icon_profile)
                .fallback(R.drawable.icon_profile)
                .into(profileImage)
            nickname.text = item.userNickname


            itemView.setOnClickListener {

                if(selectedMemberList.contains(item.userId)) {
                    selectedMemberList.remove(item.userId)
                } else {
                    selectedMemberList.add(item.userId)
                }


                if(selectedIcon.isVisible) {
                    selectedProfile.isVisible = false
                    selectedIcon.isVisible = false
                } else {
                    selectedProfile.isVisible = true
                    selectedIcon.isVisible = true
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MakeErrandFamilyAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_errand_accepter_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = familyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(familyList[position])
    }

}