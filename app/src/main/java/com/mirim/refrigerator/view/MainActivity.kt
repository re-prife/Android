package com.mirim.refrigerator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.buttonRefrigerator.setOnClickListener {
            var intent = Intent(applicationContext, RefrigeratorActivity::class.java)
            startActivity(intent)
        }
        binding.buttonHousework.setOnClickListener {
            var intent = Intent(applicationContext, HouseworkActivity::class.java)
            startActivity(intent)
        }

        binding.buttonErrand.setOnClickListener {
            var intent = Intent(applicationContext, ErrandActivity::class.java)
            startActivity(intent)
        }
        binding.buttonMakeErrand.setOnClickListener {
            var intent = Intent(applicationContext, MakeErrandActivity::class.java)
            startActivity(intent)
        }
        binding.buttonMyPage.setOnClickListener {
            var intent = Intent(applicationContext, MyPageActivity::class.java)
            startActivity(intent)
        }
    }
}