package com.mirim.refrigerator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.model.Notice
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.viewmodel.NoticeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val noticeViewModel : NoticeViewModel by viewModels()

    companion object {
        var noticeTitle: String = ""
        var noticeContent: String = ""
    }

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
        binding.writeNotice.setOnClickListener {
            var intent = Intent(applicationContext, NoticeActivity::class.java)
            startActivity(intent)
        }
        noticeViewModel.getNotice().observe(this, Observer<Notice> {
            binding.mainNoticeTitle.text = it.title
            binding.mainNoticeContent.text = it.contents
        })

    }
}