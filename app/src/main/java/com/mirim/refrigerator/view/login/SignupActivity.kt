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

        // ?????? ??????
        when {
            nameValue.isEmpty() -> {
                binding.editName.error = "????????? ???????????????."
                check = false
            }
            nickValue.isEmpty() -> {
                binding.editNickname.error = "????????? ???????????????."
                check = false
            }
            emailValue.isEmpty() -> {
                binding.editEmail.error = "???????????? ???????????????."
                check = false
            }
            !emailValue.contains('@') -> {
                binding.editEmail.error = "????????? ????????? ??????????????????."
                check = false
            }
            pwValue.isEmpty() -> {
                binding.editPassword.error = "??????????????? ???????????????."
                check = false
            }
            pwValue.length < 8 -> {
                binding.editPassword.error = "??????????????? 8??? ??????????????? ?????????."
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
                        Toast.makeText(applicationContext, "???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
                        finish()
                    }
                    400 -> {
                        Toast.makeText(applicationContext,"?????? ????????? ??????????????????.",Toast.LENGTH_SHORT).show()
                    }
                    409 -> {
                        Toast.makeText(applicationContext,"?????? ????????? ???????????????.",Toast.LENGTH_SHORT).show()
                    }

                }
                binding.progressBar.visibility = View.GONE

            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                Log.d(TAG,"fail")
                Toast.makeText(applicationContext,"??????????????? ??????????????????.",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }

        })


    }
}