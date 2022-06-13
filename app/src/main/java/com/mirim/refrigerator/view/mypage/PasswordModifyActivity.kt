package com.mirim.refrigerator.view.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityPasswordModifyBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.ModifyUserPasswordRequest
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback

class PasswordModifyActivity : AppCompatActivity() {

    lateinit var binding: ActivityPasswordModifyBinding
    val TAG = "TAG_PasswordModifyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnComplete.setOnClickListener {
            val originalValue = binding.editOriginalPassword.text.toString().trim()
            val newValue = binding.editNewPassword.text.toString().trim()
            val checkValue = binding.editCheckPassword.text.toString().trim()
            var check = true

            // 조건 확인
            when {
                originalValue.isEmpty() -> {
                    check = false
                }
                newValue.isEmpty() -> {
                    check = false
                }
                checkValue.isEmpty() -> {
                    check = false
                }
            }

            if(check) {
                applyChangedPassword(ModifyUserPasswordRequest(
                    userNewPassword = newValue,
                    userNewPasswordCheck = checkValue,
                    userPassword = originalValue
                ))
            }
        }

    }

    private fun applyChangedPassword(data : ModifyUserPasswordRequest) {
        RetrofitService.userAPI.modifyUserPassword(App.user.userId!!,data).enqueue(object : Callback<Response> {
            override fun onResponse(
                call: Call<Response>,
                response: retrofit2.Response<Response>
            ) {
                val raw = response.raw()
                Log.d("AAAAAAAAAA",raw.code().toString())
                when(raw.code()) {
                    204 -> {
                        Log.d(TAG,"갱신 성공")
                        Toast.makeText(applicationContext,"비밀번호가 성공적으로 변경되었습니다.",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    400 -> {
                        Log.e(TAG,raw.message())
                        Toast.makeText(applicationContext,"비밀번호 형식을 확인해주세요.",Toast.LENGTH_SHORT).show()
                    }
                    403 -> {
                        Log.e(TAG,raw.message())
                        Toast.makeText(applicationContext,"기존 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                    }
                    409 -> {
                        Log.e(TAG,raw.message())
                        Toast.makeText(applicationContext,"새 비밀번호가 일치하는지 확인해주세요.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(
                call: Call<Response>,
                t: Throwable
            ) {
                Log.e(TAG,"예외 발생")
            }

        })
    }
}