package com.mirim.refrigerator.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mirim.refrigerator.databinding.ActivityNoticeBinding
import com.mirim.refrigerator.model.Notice
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.NoticeViewModel
import retrofit2.Call
import retrofit2.Callback


class NoticeActivity : AppCompatActivity() {

    lateinit var binding: ActivityNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNoticeSubmit.setOnClickListener {
            writeNotice()
        }

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }


    }

    fun writeNotice() {
        RetrofitService.familyAPI.updateNotice(App.user.groupId, Notice(binding.editNoticeTitle.text.toString())).enqueue(object :
            Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Log.d("NoticeActivity", response.body().toString())
                Toast.makeText(applicationContext, "공지가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("NoticeActivity", t.toString())
            }

        })
    }
}