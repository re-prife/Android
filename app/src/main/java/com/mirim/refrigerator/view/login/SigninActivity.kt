package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivitySigninBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.SigninRequest
import com.mirim.refrigerator.server.responses.SigninResponse
import com.mirim.refrigerator.view.HomeActivity
import com.mirim.refrigerator.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {

    private val TAG : String = "TAG_SigninActivity"
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
            checkSignin()
        }



    }

    private fun checkSignin(){
        val emailValue = binding.editEmail.text.toString()
        val pwValue = binding.editPassword.text.toString()
        var check : Boolean = true

        // 조건 확인
        when {
            emailValue.isEmpty() -> {
                binding.editEmail.error = "이메일을 입력하세요."
                check = false
            }
            !emailValue.contains('@') -> {
                binding.editEmail.error = "이메일 양식을 확인해주세요."
                check = false
            }
            pwValue.isEmpty() -> {
                binding.editPassword.error = "비밀번호를 입력하세요."
                check = false
            }
        }


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
                var body = response.raw()

                when(body.code()) {
                    200 -> {
                        // TODO : 유저 객체 생성
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
                        finish()
                    }
                    400 -> {
                        Toast.makeText(applicationContext,"입력 형식을 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    403 -> {
                        Toast.makeText(applicationContext,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                    }
                    404 -> {
                        Toast.makeText(applicationContext,"입력하신 계정은 존재하지 않습니다",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                Log.d(TAG,"fail")
                Toast.makeText(applicationContext,"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }

        })
    }
}