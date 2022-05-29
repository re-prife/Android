package com.mirim.refrigerator.view.errand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityCreateErrandBinding
import com.mirim.refrigerator.databinding.ActivityIngredientDetailBinding
import com.mirim.refrigerator.view.HomeActivity

class CreateErrandActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateErrandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateErrandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // soft keyboard
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        // activity 들어온 경로 파악
        /* 0 : HomeActivity, 1 : ErrandFragment */
        var backType = intent.getIntExtra("route",0)


        binding.toolbar.toolbarTitle.text = "심부름 보내기"
        binding.toolbar.btnBack.setOnClickListener {
            if(backType == 1) {
                var intent = Intent(applicationContext, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            } else {
                finish()
            }

        }


    }
}