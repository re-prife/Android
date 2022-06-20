package com.mirim.refrigerator.view.errand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.COMPLETED
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.NOT_ACCEPTED
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.PROCEEDING
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.checkErrandStatus
import com.mirim.refrigerator.databinding.ActivityDetailedErrandInfoBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.ErrandDetailResponse
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailedErrandInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailedErrandInfoBinding
    private var questId : Int = -1
    private var acceptorId : Int? = -1
    private var errandStatus = 0
    val TAG = "DetailedErrandInfoActivity"


    override fun onResume() {
        super.onResume()
        setErrandData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedErrandInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        questId = intent.getIntExtra("questId",-1)

        binding.toolbar.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
        }

        binding.btnCancelErrand.setOnClickListener {
            acceptOrCancel()
        }

        binding.btnUpdateErrand.setOnClickListener {
            updateErrand()
        }

        binding.btnDeleteErrand.setOnClickListener {
            deleteErrand()
        }
    }

    private fun updateErrand() {
        val intent = Intent(applicationContext, CreateErrandActivity::class.java)
        intent.putExtra("isUpdate",true)
        intent.putExtra("questId",questId)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }


    private fun deleteErrand() {
        RetrofitService.errandAPI.deleteErrand(App.user.groupId,questId,App.user.userId).enqueue(object : Callback<com.mirim.refrigerator.server.responses.Response> {
            override fun onResponse(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                response: Response<com.mirim.refrigerator.server.responses.Response>
            ) {
                val raw = response.raw()
                when(raw.code()) {
                    204 -> {
                        Toast.makeText(applicationContext,"심부름을 삭제했습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                        overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
                    }
                    404 -> {
                        Toast.makeText(applicationContext,"잘못된 심부름 정보입니다.", Toast.LENGTH_SHORT).show()
                    }
                    405 -> {
                        Toast.makeText(applicationContext,"수락자가 있는 심부름은 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    409 -> {
                        Toast.makeText(applicationContext,"심부름은 요청자만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                t: Throwable
            ) {
                Toast.makeText(applicationContext,"심부름 삭제 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun setView(data : ErrandDetailResponse) {
        binding.txtErrandTitle.text = data.questTitle
        binding.txtErrandContent.text = data.questContent
        binding.txtErrandRequestedDate.text = "${data.questCreatedDate} 요청됨"


        var requesterId : Int? = App.getFamilyMember(data.requestUserId)?.userId
        var requesterText : String? = App.getFamilyMember(data.requestUserId)?.userNickname
        if(requesterId == null) {
            requesterId = App.user.userId
            requesterText = App.user.nickname
        }


        acceptorId = data.acceptUserId
        var acceptorText : String? = ""
        if(data.acceptUserId == -1)
            acceptorText = "수락자 없음"
        else if(App.getFamilyMember(data.acceptUserId)?.userNickname == null) {
            acceptorText = App.user.nickname
        } else {
            acceptorText = App.getFamilyMember(data.acceptUserId)?.userNickname
        }
        errandStatus = checkErrandStatus(data.acceptUserId,data.completeCheck)


        binding.txtRequester.text = requesterText
        binding.txtAccepter.text = acceptorText

        // 요청자가 본인일 때
        if(requesterId == App.user.userId) {

            when(errandStatus) {
                // 요청했는데 수락자가 없을 때 : 수락 대기중 + 수정 / 삭제
                NOT_ACCEPTED -> {
                    binding.statusBarWaiting.visibility = View.VISIBLE
                    binding.btnCancelErrand.visibility = View.GONE
                    binding.layoutLinearBtn.visibility = View.VISIBLE
                }
                // 요청했는데 진행중일 때 : 완료 인정해주기
                PROCEEDING -> {
                    binding.statusBarWaiting.visibility = View.GONE
                    binding.btnCancelErrand.visibility = View.VISIBLE
                    binding.layoutLinearBtn.visibility = View.GONE
                    binding.innerButton.text = "완료 인정하기"
                }
                // 요청했는데 완료된일 때 : 완료된 심부름입니다.
                COMPLETED -> {
                    binding.statusBarWaiting.visibility = View.GONE
                    binding.btnCancelErrand.visibility = View.VISIBLE
                    binding.layoutLinearBtn.visibility = View.GONE
                    binding.innerButton.text = "완료된 심부름입니다."
                    binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                }
            }
        }
        // 요청자가 본인이 아닐 때
        else {
            // 수락자가 본인일 때
            if(acceptorId == App.user.userId) {
                when(errandStatus) {
                    // 수락했는데 진행중일 때 : 수락 취소
                    PROCEEDING -> {
                        binding.innerButton.text = "수락 취소"
                        binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.main))
                    }
                    // 수락했는데 완료됨일 때 : 완료된 심부름입니다.
                    COMPLETED -> {
                        binding.innerButton.text = "완료된 심부름입니다."
                        binding.btnCancelErrand.isClickable = false
                        binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                    }
                }
            }
            // 수락자가 본인이 아니거나 없을 때
            else {
                when(errandStatus) {
                    // 수락자가 없을 때 : 수락하기
                    NOT_ACCEPTED -> {
                        binding.innerButton.text = "수락하기"
                        binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.main))
                    }
                    // 진행중일 때 : 다른 멤버가 진행중인 심부름입니다.
                    PROCEEDING -> {
                        binding.innerButton.text = "다른 멤버가 진행중인 심부름입니다."
                        binding.btnCancelErrand.isClickable = false
                        binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                    }
                    // 완료됨일 때 : 다른 멤버가 완료한 심부름입니다.
                    COMPLETED -> {
                        binding.innerButton.text = "다른 멤버가 완료한 심부름입니다."
                        binding.btnCancelErrand.isClickable = false
                        binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                    }
                }
            }

        }
    }


    private fun setErrandData() {
        RetrofitService.errandAPI.getErrandDetail(App.user.groupId,questId).enqueue(object : Callback<ErrandDetailResponse> {
            override fun onResponse(
                call: Call<ErrandDetailResponse>,
                response: Response<ErrandDetailResponse>
            ) {
                val body = response.body()
                val raw = response.raw()

                when(raw.code()) {
                    200 -> {
                        acceptorId = body?.acceptUserId!!
                        setView(body)
                    }
                    404 -> {
                        Toast.makeText(applicationContext,"심부름 정보가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ErrandDetailResponse>, t: Throwable) {
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun acceptOrCancel() {
        acceptorId = App.user.userId!!
        RetrofitService.errandAPI.acceptOrCancel(App.user.groupId,questId,acceptorId)
            .enqueue(object : Callback<com.mirim.refrigerator.server.responses.Response> {
                override fun onResponse(
                    call: Call<com.mirim.refrigerator.server.responses.Response>,
                    response: Response<com.mirim.refrigerator.server.responses.Response>
                ) {
                    val raw = response.raw()
                    when(raw.code()) {
                        200 -> {
                            if(errandStatus == PROCEEDING) {
                                Toast.makeText(applicationContext,"심부름을 취소하였습니다.",Toast.LENGTH_SHORT).show()
                            } else if(errandStatus == NOT_ACCEPTED) {
                                Toast.makeText(applicationContext,"심부름을 수락하였습니다.",Toast.LENGTH_SHORT).show()
                            }
                            finish()
                            overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
                        }
                        404 -> {
                            Toast.makeText(applicationContext,"잘못된 심부름 정보입니다.",Toast.LENGTH_SHORT).show()
                        }
                        409 -> {
                            Toast.makeText(applicationContext,"이미 해결되거나 수락자가 존재하는 심부름입니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<com.mirim.refrigerator.server.responses.Response>,
                    t: Throwable
                ) {
                    Toast.makeText(applicationContext,"심부름에 접근할 수 없습니다.",Toast.LENGTH_SHORT).show()
                    Log.e(TAG,t.message.toString())
                }

            })
    }

}