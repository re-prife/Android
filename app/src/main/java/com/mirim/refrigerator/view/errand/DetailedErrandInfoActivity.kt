package com.mirim.refrigerator.view.errand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.COMPLETED
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.NOT_ACCEPTED
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.PROCEEDING
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.checkErrandStatus
import com.mirim.refrigerator.databinding.ActivityDetailedErrandInfoBinding
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.ErrandDetailResponse
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailedErrandInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailedErrandInfoBinding
    private var questId : Int = -1
    private var acceptorId : Int = -1
    private var errandStatus = 0
    val TAG = "DetailedErrandInfoActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedErrandInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        questId = intent.getIntExtra("questId",-1)
        setErrandData()

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCancelErrand.setOnClickListener {
            acceptOrCancel()
        }
    }
    private fun setView(data : ErrandDetailResponse) {
        binding.txtErrandTitle.text = data.questTitle
        binding.txtErrandContent.text = data.questContent
        binding.txtErrandRequestedDate.text = data.questCreatedDate

        val requestUser : FamilyMember? = App.getFamilyMember(data.requestUserId)
        val acceptUser : FamilyMember? = App.getFamilyMember(data.acceptUserId)
        errandStatus = checkErrandStatus(data.acceptUserId,data.completeCheck)
        when(errandStatus) {
            PROCEEDING -> {
                if(acceptUser?.userNickname == null) {
                    binding.innerButton.text = "수락 취소"
                    binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.main))
                    binding.txtRequester.text = requestUser?.userNickname ?: App.user.nickname
                    binding.txtAccepter.text = acceptUser?.userNickname ?: App.user.nickname
                } else {
                    binding.innerButton.text = "진행중인 심부름입니다."
                    binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                    binding.txtRequester.text = requestUser?.userNickname ?: App.user.nickname
                    binding.txtAccepter.text = acceptUser.userNickname ?: App.user.nickname
                }
            }
            NOT_ACCEPTED -> {
                binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.main))
                binding.txtRequester.text = requestUser?.userNickname ?: App.user.nickname
                binding.txtAccepter.text = "수락자 없음"
                binding.innerButton.text = "수락하기"
            }
            COMPLETED -> {
                binding.innerButton.text = "완료된 심부름입니다."
                binding.btnCancelErrand.setBackgroundColor(applicationContext.resources.getColor(R.color.gray))
                binding.txtRequester.text =requestUser?.userNickname ?: App.user.nickname
                binding.txtAccepter.text = acceptUser?.userNickname ?: App.user.nickname
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

                when(raw.code) {
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
                    when(raw.code) {
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