package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivitySignupBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.SignupRequest
import com.mirim.refrigerator.server.responses.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    val TAG = "TAG_SIGNUPACTIVITY"
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnSignin.setOnClickListener {
            var intent = Intent(applicationContext,SigninActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
            finish()
        }
        binding.btnSignup.setOnClickListener {
//            var intent = Intent(applicationContext,SelectRegisterTypeActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
//            overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
//            finish()
            checkSignup()
        }

    }

    fun checkSignup() {
        val nameValue : String = binding.editName.text.toString()
        val nickValue : String = binding.editNickname.text.toString()
        val emailValue : String = binding.editEmail.text.toString()
        val pwValue : String = binding.editPassword.text.toString()
        var check : Boolean = false

        //TODO : 이메일 양식 및 조건 확인
        check = true

        if(check){
            val data = SignupRequest(emailValue,nameValue,nickValue,pwValue)
            progressSignup(data)
        }


    }

    private fun progressSignup(data : SignupRequest) {
        RetrofitService.serviceAPI.signup(data).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {

                var body = response.body()
                Log.d(TAG,response.toString())
                Log.d(TAG,response.body()?.status.toString())

                when(body?.status) {
                    201 -> null // TODO : 회원가입 성공
                    400 -> null // TODO : 회원가입 실패 - request 형식 오류
                    409 -> null // TODO : 회원가입 실패 - 존재하는 Email
                }

            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                Log.d(TAG,"fail")
                // TODO : fail된 경우 처리
            }

        })


    }
}