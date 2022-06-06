package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivitySigninBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.SigninRequest
import com.mirim.refrigerator.server.responses.SigninResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {

    private val TAG : String = "SigninActivity"
    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.btnSignup.setOnClickListener{
            var intent = Intent(applicationContext,SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
            finish()
        }
        binding.btnSignin.setOnClickListener {
            /*
            var intent = Intent(applicationContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
            finish()
             */
            checkSignin()
        }



    }

    private fun checkSignin(){
        val emailValue = binding.editEmail.text.toString()
        val pwValue = binding.editPassword.text.toString()
        var check : Boolean = false

        if(emailValue.isEmpty()) {
            // TODO : 이메일 입력 필요 알림
            Log.d(TAG,"이메일 입력 필요")
        } else if(pwValue.isEmpty()) {
            // TODO : 비밀번호 입력 필요 알림
            Log.d(TAG,"비밀번호 입력 필요")
        } else if(pwValue.length < 5){
            // TODO : 비밀번호 조건 일치 필요 알림
            Log.d(TAG,"비밀번호 조건 필요")
        } else check = true


        if(check) {
            val data = SigninRequest(emailValue,pwValue)
            progressLogin(data)
        }

    }

    private fun progressLogin(data : SigninRequest) {
        RetrofitService.serviceAPI.signin(data).enqueue(object : Callback<SigninResponse> {
            override fun onResponse(
                call: Call<SigninResponse>,
                response: Response<SigninResponse>
            ) {
                var body = response.body()
                Log.d(TAG,response.toString())
                Log.d(TAG,response.body()?.status.toString())

                when(body?.status) {
                    200 -> null // TODO : 로그인 성공
                    400 -> null // TODO : 로그인 실패 - request 형식 오류
                    403 -> null // TODO : 로그인 실패 - 회원 정보 불일치
                    404 -> null // TODO : 로그인 실패 - email 존재하지 않음
                }
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                Log.d(TAG,"fail")
                // TODO : fail된 경우 처리
            }

        })
    }
}