package com.mirim.refrigerator.view.housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityCertifyChoreBinding

class CertifyChoreActivity : AppCompatActivity() {
    lateinit var binding: ActivityCertifyChoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertifyChoreBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.root)

        var title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val nickname = intent.getStringExtra("nickname")
        Log.d("CertifyChoreActivity", title!!)

        binding.txtHouseworkCategory.text = category
        binding.txtHouseworkTitle.text = nickname+"님이 " + title+"을 완료했습니다."

        binding.btnApprove.setOnClickListener {

        }

        binding.btnDismiss.setOnClickListener {

        }

    }
}