package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivitySignupBinding
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.SignupRequest
import com.mirim.refrigerator.server.responses.SignupResponse
import com.mirim.refrigerator.viewmodel.UserViewModel
import com.mirim.refrigerator.viewmodel.App
import org.w3c.dom.Text
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
            binding.progressBar.visibility = View.VISIBLE
            checkSignup()
        }

    }


    private fun checkSignup() {
        val nameValue : String = binding.editName.text.toString().trim()
        val nickValue : String = binding.editNickname.text.toString().trim()
        val emailValue : String = binding.editEmail.text.toString().trim()
        val pwValue : String = binding.editPassword.text.toString().trim()
        var check = true

        // 조건 확인
        when {
            nameValue.isEmpty() -> {
                binding.editName.error = "이름을 입력하세요."
                check = false
            }
            nickValue.isEmpty() -> {
                binding.editNickname.error = "별명을 입력하세요."
                check = false
            }
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
            pwValue.length < 8 -> {
                binding.editPassword.error = "비밀번호는 8자 이상이어야 합니다."
                check = false
            }
        }

        if(check){
            val data = SignupRequest(emailValue,nameValue,nickValue,pwValue)
            progressSignup(data)

        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun progressSignup(data : SignupRequest) {
        RetrofitService.serviceAPI.signup(data).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                val raw = response.raw()
                val body = response.body()

                when(raw.code()) {
                    201 -> {
                        val userData = User(body?.userNickname,body?.userName,body?.userEmail,body?.userId,null, "")

                        App.user = userData

                        val intent = Intent(applicationContext,SelectRegisterTypeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
                        finish()
                    }
                    400 -> {
                        Toast.makeText(applicationContext,"입력 형식을 확인해주세요.",Toast.LENGTH_SHORT).show()
                    }
                    409 -> {
                        Toast.makeText(applicationContext,"이미 등록된 계정입니다.",Toast.LENGTH_SHORT).show()
                    }

                }
                binding.progressBar.visibility = View.GONE

            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                Log.d(TAG,"fail")
                Toast.makeText(applicationContext,"회원가입에 실패했습니다.",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }

        })


    }
}