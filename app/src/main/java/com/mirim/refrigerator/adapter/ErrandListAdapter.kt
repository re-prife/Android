package com.mirim.refrigerator.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.Errand
import com.mirim.refrigerator.view.errand.DetailedErrandInfoActivity
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.UserViewModel

class ErrandListAdapter (val context: Context?, private val errandList : List<Errand>) :
    RecyclerView.Adapter<ErrandListAdapter.ViewHolder>() {

    private var userViewModel = UserViewModel()
    companion object {
        const val NOT_ACCEPTED = 153
        const val PROCEEDING = 301
        const val COMPLETED = 240

        fun checkErrandStatus(id : Int?, iscomplete : Boolean) : Int {
            return if(iscomplete) {
                COMPLETED
            } else if(id==-1) {
                NOT_ACCEPTED
            } else {
                PROCEEDING
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val requesterAndAccepter : TextView = itemView.findViewById(R.id.txt_requester_accepter)
        val title : TextView = itemView.findViewById(R.id.txt_title)
        val button : RelativeLayout = itemView.findViewById(R.id.btn_accept)
        val statusBar : View = itemView.findViewById(R.id.status_bar)
        val buttonText : TextView = itemView.findViewById(R.id.txt_accept)


        @SuppressLint("SetTextI18n")
        fun bind(item: Errand, status: Int, requesterNickname : String? ,accepterNickname: String?) {

            title.text = item.questTitle

            when(status) {
                NOT_ACCEPTED -> {
                    requesterAndAccepter.text =
                        "$requesterNickname, 수락자 없음"
                    button.isEnabled = true
                    button.setBackgroundColor(context!!.resources.getColor(R.color.main))
                    statusBar.setBackgroundColor(context.resources.getColor(R.color.black))
                    buttonText.text = "수락"
                }
                PROCEEDING -> {
                    requesterAndAccepter.text =
                        "$requesterNickname > $accepterNickname"
                    button.isEnabled = false
                    button.setBackgroundColor(context!!.resources.getColor(R.color.deep_gray))
                    statusBar.setBackgroundColor(context.resources.getColor(R.color.deep_gray))
                    buttonText.text = "수락됨"
                }
                COMPLETED -> {
                    requesterAndAccepter.text =
                        "$requesterNickname > $accepterNickname"
                    button.visibility = View.GONE
                    statusBar.setBackgroundColor(context!!.resources.getColor(R.color.deep_gray))
                }
            }

            itemView.setOnClickListener {
                val intent = Intent(context,DetailedErrandInfoActivity::class.java)
                intent.putExtra("questId",item.questId)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                ContextCompat.startActivity(context!!,intent,null)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ErrandListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_errand_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ErrandListAdapter.ViewHolder, position: Int) {
        userViewModel.loadUsers(App.user)
        userViewModel.setFamilyList(App.family)

        val requestUserId = errandList[position].requestUserId
        val acceptUserId : Int? = errandList[position].acceptUserId


        var requesterNickname = userViewModel.getFamilyMember(memberId = requestUserId)?.userNickname
        if(requesterNickname == null)
            requesterNickname = userViewModel.getUser().value?.nickname
        var accepterNickname = userViewModel.getFamilyMember(memberId = acceptUserId)?.userNickname
        if(accepterNickname == null)
            accepterNickname = userViewModel.getUser().value?.nickname


        if(errandList[position].completeCheck) {
            holder.bind(errandList[position], COMPLETED, requesterNickname, accepterNickname )
        } else if(errandList[position].acceptUserId == -1) {
            holder.bind(errandList[position], NOT_ACCEPTED, requesterNickname, accepterNickname )
        } else {
            holder.bind(errandList[position], PROCEEDING, requesterNickname, accepterNickname )
        }
    }

    override fun getItemCount(): Int = errandList.size
}