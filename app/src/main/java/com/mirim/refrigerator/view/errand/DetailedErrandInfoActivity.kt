package com.mirim.refrigerator.view.errand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.COMPLETED
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.NOT_ACCEPTED
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.PROCEEDING
import com.mirim.refrigerator.adapter.ErrandListAdapter.Companion.checkErrandStatus
import com.mirim.refrigerator.databinding.ActivityDetailedErrandInfoBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.ErrandDetailResponse
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailedErrandInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailedErrandInfoBinding
    private val userViewModel : UserViewModel by viewModels()
    private var questId : Int = -1
    private var acceptorId : Int = -1
    private var errandStatus = 0
    val TAG = "DetailedErrandInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedErrandInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        questId = intent.getIntExtra("questId",0)

        initData()
        setErrandData()

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCancelErrand.setOnClickListener {
            RetrofitService.errandAPI.acceptOrCancel(userViewModel.getGroupId(),questId,acceptorId)
                .enqueue(object : Callback<com.mirim.refrigerator.server.responses.Response> {
                    override fun onResponse(
                        call: Call<com.mirim.refrigerator.server.responses.Response>,
                        response: Response<com.mirim.refrigerator.server.responses.Response>
                    ) {
                        val raw = response.raw()
                        when(raw.code()) {
                            200 -> {
                                if(errandStatus == PROCEEDING) {
                                    Toast.makeText(applicationContext,"심부름이 성공적으로 취소되었습니다.",Toast.LENGTH_SHORT).show()
                                } else if(errandStatus == NOT_ACCEPTED) {
                                    Toast.makeText(applicationContext,"심부름이 성공적으로 수락되었습니다.",Toast.LENGTH_SHORT).show()
                                }
                            }
                            404 -> {
                                Toast.makeText(applicationContext,raw.message(),Toast.LENGTH_SHORT).show()
                            }
                            409 -> {
                                Toast.makeText(applicationContext,raw.message(),Toast.LENGTH_SHORT).show()
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
    private fun initData() {
        userViewModel.loadUsers(App.user)
        userViewModel.setFamilyList(App.family)
    }
    private fun setView(data : ErrandDetailResponse) {
        binding.txtErrandTitle.text = data.questTitle
        binding.txtErrandContent.text = "content"
        binding.txtErrandRequestedDate.text = data.questCreatedDate

        val userValue = userViewModel.getFamilyMember(data.requestUserId)
        errandStatus = checkErrandStatus(data.acceptUserId,data.completeCheck)
        when(errandStatus) {
            PROCEEDING -> {
                binding.txtRequester.text =
                    if(userValue==null)
                        userValue?.nickname
                    else
                        userViewModel.getUser().value?.nickname
                binding.txtAccepter.text =
                    if(userValue==null)
                        userValue?.nickname
                    else
                        userViewModel.getUser().value?.nickname
            }
            NOT_ACCEPTED -> {
                binding.btnCancelErrand.visibility = View.GONE
                binding.txtRequester.text =
                    if(userValue==null)
                        userValue?.nickname
                    else
                        userViewModel.getUser().value?.nickname
                binding.txtAccepter.text = "수락자 없음"
            }
            COMPLETED -> {
                binding.btnCancelErrand.visibility = View.GONE
                binding.txtRequester.text =
                    if(userValue==null)
                        userValue?.nickname
                    else
                        userViewModel.getUser().value?.nickname
                binding.txtAccepter.text =
                    if(userValue==null)
                        userValue?.nickname
                    else
                        userViewModel.getUser().value?.nickname
            }
        }
    }


    private fun setErrandData() {
        RetrofitService.errandAPI.getErrandDetail(userViewModel.getGroupId()!!,questId).enqueue(object : Callback<ErrandDetailResponse> {
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
                        Toast.makeText(applicationContext,raw.message(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ErrandDetailResponse>, t: Throwable) {
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

}