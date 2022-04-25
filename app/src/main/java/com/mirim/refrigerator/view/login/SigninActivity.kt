package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.databinding.ActivityMainBinding
import com.mirim.refrigerator.databinding.ActivitySigninBinding
import com.mirim.refrigerator.view.MainActivity

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)


        binding.btnSignup.setOnClickListener{
            var intent = Intent(applicationContext,SignupActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignin.setOnClickListener {
            var intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }



    }
}