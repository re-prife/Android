package com.mirim.refrigerator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mirim.refrigerator.R
import com.mirim.refrigerator.dialog.HouseworkDetailDialog
import com.mirim.refrigerator.model.Housework
import com.mirim.refrigerator.viewmodel.App

class HouseworkAdapter(val context: Context?, val chores: List<Housework>?): RecyclerView.Adapter<HouseworkAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtChoreTitle = view.findViewById<TextView>(R.id.txt_housework_name)
        val txtChoreAssignee = view.findViewById<TextView>(R.id.txt_housework_assignee)
        val txtChoreCategory = view.findViewById<TextView>(R.id.txt_housework_category)

        fun bind(chore: Housework?) {
            txtChoreTitle.text = chore?.choreTitle
            txtChoreAssignee.text = (if (App.getFamilyMember(chore?.userId) == null) App.user.nickname else App.getFamilyMember(chore?.userId)?.userNickname) + "담당"
            txtChoreCategory.text = Housework.categoryKoreanConverter(chore?.choreCategory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_housework_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chores?.get(position))
        holder.view.setOnClickListener {
            val dialog = HouseworkDetailDialog(chores?.get(position))
            dialog.show((context as AppCompatActivity).supportFragmentManager, "chore")
        }
    }

    override fun getItemCount(): Int {
        return chores!!.size
    }
}