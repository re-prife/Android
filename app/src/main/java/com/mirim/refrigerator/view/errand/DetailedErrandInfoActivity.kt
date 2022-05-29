package com.mirim.refrigerator.view.errand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityDetailedErrandInfoBinding
import com.mirim.refrigerator.databinding.ActivityIngredientDetailBinding

class DetailedErrandInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailedErrandInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedErrandInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}