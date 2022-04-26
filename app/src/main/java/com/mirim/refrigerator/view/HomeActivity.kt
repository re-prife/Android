package com.mirim.refrigerator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        var intent = Intent(applicationContext, BottomAppBarActivity::class.java)

        binding.buttonRefrigerator.setOnClickListener {
            intent.putExtra("clicked button", "refrigerator")
            startActivity(intent)
        }
        binding.buttonHousework.setOnClickListener {
            intent.putExtra("clicked button", "housework")
            startActivity(intent)
        }

        binding.buttonErrand.setOnClickListener {
            intent.putExtra("clicked button", "errand")
            startActivity(intent)
        }
        binding.buttonMakeErrand.setOnClickListener {
            intent.putExtra("clicked button", "make errand")
            startActivity(intent)
        }
        binding.buttonMyPage.setOnClickListener {
            intent.putExtra("clicked button", "my page")
            startActivity(intent)
        }
    }
}