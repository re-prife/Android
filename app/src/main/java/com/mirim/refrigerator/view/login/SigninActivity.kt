package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivitySigninBinding
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.SigninRequest
import com.mirim.refrigerator.server.responses.SigninResponse
import com.mirim.refrigerator.view.HomeActivity
import com.mirim.refrigerator.viewmodel.UserViewModel
import com.mirim.refrigerator.viewmodel.App
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
            val intent = Intent(applicationContext,SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
            finish()
        }
        binding.btnSignin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
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
                binding.progressBar.visibility = View.GONE
            }
            !emailValue.contains('@') -> {
                binding.editEmail.error = "이메일 양식을 확인해주세요."
                check = false
                binding.progressBar.visibility = View.GONE
            }
            pwValue.isEmpty() -> {
                binding.editPassword.error = "비밀번호를 입력하세요."
                check = false
                binding.progressBar.visibility = View.GONE
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
                val raw = response.raw()
                val body = response.body()

                when(raw.code()) {
                    200 -> {
                        App.user = User(body?.userNickname, body?.userName, body?.userEmail, body?.userId, body?.groupId, body?.userImagePath)
                        UserViewModel().loadUsers(App.user)
                        App.groupInviteCode = body?.groupInviteCode.toString()

                        // 그룹 가입 여부 확인
                        if(body?.groupId == null) {
                            val intent = Intent(applicationContext,SelectRegisterTypeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(applicationContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                            overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
                            finish()
                        } else {
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(applicationContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                            overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
                            finish()
                        }
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
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                Toast.makeText(applicationContext,"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }

        })
    }
}