package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mirim.refrigerator.databinding.ActivityInputGroupNameBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.CreateGroupRequest
import com.mirim.refrigerator.server.responses.CreateGroupResponse
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.App.groupInviteCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputGroupNameActivity : AppCompatActivity() {

    private val TAG : String = "TAG_InputGroupNameActivity"
    private lateinit var binding: ActivityInputGroupNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputGroupNameBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnJoinGroup.setOnClickListener {
            checkJoinGroup()
        }

    }

    private fun checkJoinGroup(){
        val groupNameValue : String = binding.edittxtInputName.text.toString().trim()
        var check : Boolean = true

        // 조건 확인
        if(groupNameValue.isEmpty()) {
            binding.edittxtInputName.error = "그룹 이름을 입력해주세요."
            check = false
        }

        if(check) {
            val data = CreateGroupRequest(groupNameValue)
            progressCreateGroup(data)
        }

    }
    private fun progressCreateGroup(data : CreateGroupRequest) {
        RetrofitService.serviceAPI.createGroup(App.user.userId.toString(),data).enqueue(object : Callback<CreateGroupResponse> {
            override fun onResponse(
                call: Call<CreateGroupResponse>,
                response: Response<CreateGroupResponse>
            ) {
                val raw = response.raw()

                when(raw.code) {
                    201 -> {
                        App.user.groupId = response.body()?.groupId
                        App.groupInviteCode = response.body()?.groupInviteCode.toString()
                        val intent = Intent(applicationContext,ShowGroupCodeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                    400 -> {
                        Toast.makeText(applicationContext,raw.message, Toast.LENGTH_SHORT).show()
                    }
                    404 -> {
                        Toast.makeText(applicationContext,raw.message, Toast.LENGTH_SHORT).show()
                    }
                    409 -> {
                        Toast.makeText(applicationContext,raw.message, Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onFailure(call: Call<CreateGroupResponse>, t: Throwable) {
                Toast.makeText(applicationContext,"그룹 생성에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        })

    }
}

