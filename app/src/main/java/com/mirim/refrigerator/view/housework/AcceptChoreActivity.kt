package com.mirim.refrigerator.view.housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityAcceptChoreBinding
import com.mirim.refrigerator.model.Housework

class AcceptChoreActivity : AppCompatActivity() {
    lateinit var binding: ActivityAcceptChoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcceptChoreBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val category = intent.getStringExtra("category")
        val title = intent.getStringExtra("title")

        binding.txtHouseworkCategory.text = Housework.categoryKoreanConverter(category)
        binding.txtHouseworkTitle.text = title + " 인증이 수락되었습니다."

        binding.btnConfirm.setOnClickListener {
            finish()
        }


    }
}