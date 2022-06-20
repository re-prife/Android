package com.mirim.refrigerator.view.errand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityAcceptErrandBinding

class AcceptErrandActivity : AppCompatActivity() {

    lateinit var binding : ActivityAcceptErrandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcceptErrandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val accepter = intent.getStringExtra("nickname")

        binding.txtTitle.text = title
        binding.txtAccepter.text = "by $accepter"

        binding.btnSave.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.translate_none, R.anim.translate_none)
        }
    }
}