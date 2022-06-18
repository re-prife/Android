package com.mirim.refrigerator.view.errand

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirim.refrigerator.R
import com.mirim.refrigerator.adapter.MakeErrandFamilyAdapter
import com.mirim.refrigerator.adapter.MakeErrandFamilyAdapter.Companion.selectedMemberList
import com.mirim.refrigerator.databinding.ActivityCreateErrandBinding
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.MakeErrandRequest
import com.mirim.refrigerator.view.BottomAppBarActivity
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class CreateErrandActivity : AppCompatActivity() {


    private val userViewModel : UserViewModel by viewModels()
    private var backType : Int = 0
    lateinit var accepterAdapter : MakeErrandFamilyAdapter


    companion object {
        lateinit var binding: ActivityCreateErrandBinding
    }

    override fun onResume() {
        super.onResume()
        // soft keyboard
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        // activity 들어온 경로 파악 0 : HomeActivity, 1 : ErrandFragment
        backType = intent.getIntExtra("route",0)
        binding.toolbar.toolbarTitle.text = "심부름 보내기"
        userViewModel.loadUsers(App.user)
        setFamilyMember()
        selectedMemberList = ArrayList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateErrandBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.toolbar.btnBack.setOnClickListener {
            if(backType == 0) {
                val intent = Intent(applicationContext, BottomAppBarActivity::class.java)
                intent.putExtra("clicked button", "errand")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            } else {
                finish()
                overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
            }
        }

        binding.btnSendErrand.setOnClickListener {
            sendErrand()
        }
    }

    private fun sendErrand() {
        val titleValue = binding.editErrandTitle.text.toString().trim()
        val contentValue = binding.editErrandContent.text.toString().trim()

        Log.e("AAAAAAA", MakeErrandFamilyAdapter.selectedMemberList.toString())
        val data = MakeErrandRequest(contentValue,titleValue,MakeErrandFamilyAdapter.selectedMemberList)

        RetrofitService.errandAPI.makeErrand(userViewModel.getGroupId()!!,userViewModel.getUserId()!!,data).enqueue(object :
            Callback<com.mirim.refrigerator.server.responses.Response>{
            override fun onResponse(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                response: Response<com.mirim.refrigerator.server.responses.Response>
            ) {
                val raw = response.raw()
                when(raw.code) {
                    201 -> {
                        Toast.makeText(applicationContext,"심부름이 생성되었습니다.",Toast.LENGTH_SHORT).show()

                        if(backType == 0) {
                            val intent = Intent(applicationContext, BottomAppBarActivity::class.java)
                            intent.putExtra("clicked button", "errand")
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                        } else {
                            finish()
                            overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
                        }
                    }
                    400 -> {
                        Toast.makeText(applicationContext,"심부름 형식을 확인해주세요.",Toast.LENGTH_SHORT).show()
                    }
                    404 -> {
                        Toast.makeText(applicationContext,"그룹 또는 유저가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                t: Throwable
            ) {
                Toast.makeText(applicationContext,"심부름 생성에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }

        })


    }


    private fun setFamilyMember() {
        RetrofitService.familyAPI.getFamilyList(userViewModel.getGroupId(),userViewModel.getUserId()).enqueue(object :
            Callback<List<FamilyMember>> {
            override fun onResponse(
                call: Call<List<FamilyMember>>,
                response: Response<List<FamilyMember>>
            ) {
                val body = response.body()
                // 다른 멤버 존재 여부 확인
                if(body?.size==0) {
                    binding.btnSendErrand.isEnabled = false
                    binding.btnSendErrand.setBackgroundColor(Color.parseColor(R.color.deep_deep_gray.toString()))
                    binding.btnSendErrand.text = "그룹원이 없어 심부름 요청이 불가능합니다."
                } else {
                    App.family = response.body()!!
                    userViewModel.setFamilyList(response.body()!!)
                    accepterAdapter = MakeErrandFamilyAdapter(applicationContext,userViewModel.getFamily())
                    binding.listRecycleAccepter.adapter = accepterAdapter
                    binding.listRecycleAccepter.layoutManager = LinearLayoutManager(applicationContext,RecyclerView.HORIZONTAL,false)
                }
            }
            override fun onFailure(call: Call<List<FamilyMember>>, t: Throwable) {

                Toast.makeText(applicationContext,"그룹원을 불러오는데에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        })
    }
}