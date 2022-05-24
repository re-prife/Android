package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.databinding.ActivityQrScanBinding

class QrScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScanBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)



    }
}