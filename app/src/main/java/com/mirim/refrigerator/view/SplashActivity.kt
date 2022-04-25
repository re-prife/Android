package com.mirim.refrigerator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivitySigninBinding
import com.mirim.refrigerator.databinding.ActivitySplashBinding
import com.mirim.refrigerator.view.login.SigninActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Handler().postDelayed(Runnable {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
            finish()
        },3000)
    }
}