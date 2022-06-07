package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mirim.refrigerator.databinding.ActivityInputGroupCodeBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.CreateGroupRequest
import com.mirim.refrigerator.server.request.JoinGroupRequest
import com.mirim.refrigerator.server.responses.CreateGroupResponse
import com.mirim.refrigerator.server.responses.JoinGroupResponse
import com.mirim.refrigerator.view.HomeActivity
import com.mirim.refrigerator.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputGroupCodeActivity : AppCompatActivity() {

    private val TAG : String = "TAG_InputGroupCodeActivity"
    private lateinit var binding: ActivityInputGroupCodeBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputGroupCodeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding.btnJoinGroup.setOnClickListener {
            checkJoinGroup()
        }

    }

    private fun checkJoinGroup(){
        val groupCodeValue : String = binding.edittxtInputCode.text.toString().trim()
        var check : Boolean = true

        // 조건 확인
        if(groupCodeValue.isEmpty()) {
            binding.edittxtInputCode.error = "그룹 코드를 입력해주세요."
            check = false
        }

        if(check) {
            val data = JoinGroupRequest(groupCodeValue)
            progressCreateGroup(data)
            userViewModel.setGroupId(data.groupInviteCode)
        }

    }
    private fun progressCreateGroup(data : JoinGroupRequest) {
        Log.d(TAG,userViewModel.getUserId().toString())
        RetrofitService.serviceAPI.joinGroup(userViewModel.getUserId().toString(),data).enqueue(object : Callback<JoinGroupResponse> {
            override fun onResponse(
                call: Call<JoinGroupResponse>,
                response: Response<JoinGroupResponse>
            ) {
                val raw = response.raw()
                Log.d(TAG,response.toString())

                when(raw.code()) {
                    200 -> {
                        Log.d(TAG,"그룹 참여 성공")
                        val intent = Intent(applicationContext,SigninActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                    400 -> {
                        Toast.makeText(applicationContext,raw.message(), Toast.LENGTH_SHORT).show()
                    }
                    404 -> {
                        Toast.makeText(applicationContext,raw.message(), Toast.LENGTH_SHORT).show()
                    }
                    409 -> {
                        Toast.makeText(applicationContext,raw.message(), Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onFailure(call: Call<JoinGroupResponse>, t: Throwable) {

            }
        })

    }
}

