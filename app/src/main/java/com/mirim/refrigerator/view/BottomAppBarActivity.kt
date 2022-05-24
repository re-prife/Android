package com.mirim.refrigerator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityBottomAppBarBinding
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.view.fragment.ErrandFragment
import com.mirim.refrigerator.view.fragment.HouseworkFragment
import com.mirim.refrigerator.view.fragment.MyPageFragment
import com.mirim.refrigerator.view.fragment.RefrigeratorFragment

class BottomAppBarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomAppBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomAppBarBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        var clickedButton = intent.getStringExtra("clicked button")

        when(clickedButton) {
            "refrigerator" -> {
                supportFragmentManager.beginTransaction().add(R.id.frame, RefrigeratorFragment()).commit()
            }
            "housework" -> {
                supportFragmentManager.beginTransaction().add(R.id.frame, HouseworkFragment()).commit()
            }
            "errand" -> {
                supportFragmentManager.beginTransaction().add(R.id.frame, ErrandFragment()).commit()
            }
            "make errand" -> {
                supportFragmentManager.beginTransaction().add(R.id.frame, ErrandFragment()).commit()
            }
            "my page" -> {
                supportFragmentManager.beginTransaction().add(R.id.frame, MyPageFragment()).commit()
            }
            else -> {
                supportFragmentManager.beginTransaction().add(R.id.frame, RefrigeratorFragment()).commit()
            }
        }



        binding.refrigerator.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frame, RefrigeratorFragment()).commit()
        }
        binding.housework.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frame, HouseworkFragment()).commit()
        }
        binding.errand.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frame, ErrandFragment()).commit()
        }
        binding.mypage.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frame, MyPageFragment()).commit()
        }

        binding.fab.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
        }


    }
}