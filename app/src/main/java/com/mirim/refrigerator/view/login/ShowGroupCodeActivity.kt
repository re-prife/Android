package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mirim.refrigerator.databinding.ActivityShowGroupCodeBinding
import com.mirim.refrigerator.view.HomeActivity
import com.mirim.refrigerator.viewmodel.UserViewModel
import com.mirim.refrigerator.viewmodel.App

class ShowGroupCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowGroupCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowGroupCodeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.textGroupCode.text = App.groupInviteCode
        binding.btnSignin.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}