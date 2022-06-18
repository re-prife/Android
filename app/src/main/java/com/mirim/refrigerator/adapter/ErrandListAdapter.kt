package com.mirim.refrigerator.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.Errand
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.view.errand.DetailedErrandInfoActivity
import com.mirim.refrigerator.view.fragment.ErrandFragment
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback

class ErrandListAdapter (val context: Context?, private val errandList : List<Errand>, private val fragment : Fragment, private val fragmentManager: FragmentManager) :
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

            button.setOnClickListener {
                RetrofitService.errandAPI.acceptOrCancel(App.user.groupId,item.questId,App.user.userId)
                    .enqueue(object : Callback<Response> {
                        override fun onResponse(
                            call: Call<Response>,
                            response: retrofit2.Response<Response>
                        ) {
                            val raw = response.raw()
                            when(raw.code) {
                                200 -> {
                                    if(status == PROCEEDING) {
                                        Toast.makeText(context,"심부름을 취소하였습니다.", Toast.LENGTH_SHORT).show()
                                    } else if(status == NOT_ACCEPTED) {
                                        Toast.makeText(context,"심부름을 수락하였습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                    ErrandFragment.refreshFragment(fragment)
                                }
                                404 -> {
                                    Toast.makeText(context,"잘못된 심부름 정보입니다.", Toast.LENGTH_SHORT).show()
                                }
                                409 -> {
                                    Toast.makeText(context,"이미 해결되거나 수락자가 존재하는 심부름입니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<Response>,
                            t: Throwable
                        ) {
                            Toast.makeText(context,"심부름에 접근할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }

                    })
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