package com.mirim.refrigerator.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mirim.refrigerator.databinding.ActivityNoticeBinding
import com.mirim.refrigerator.model.Notice
import com.mirim.refrigerator.viewmodel.NoticeViewModel


class NoticeActivity : AppCompatActivity() {

    lateinit var binding: ActivityNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNoticeSubmit.setOnClickListener {
            // server 통신
            Toast.makeText(applicationContext, "공지가 등록되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}