package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityInputGroupCodeBinding
import com.mirim.refrigerator.view.MainActivity

class InputGroupCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputGroupCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputGroupCodeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnJoinGroup.setOnClickListener {
            //TODO : 존재하는 그룹 코드인지 확인
            var intent = Intent(applicationContext,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

    }
}