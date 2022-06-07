package com.mirim.refrigerator.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mirim.refrigerator.databinding.ActivityShowGroupCodeBinding
import com.mirim.refrigerator.view.HomeActivity
import com.mirim.refrigerator.viewmodel.UserViewModel

class ShowGroupCodeActivity : AppCompatActivity() {

    private val TAG : String = "TAG_ShowGroupCodeActivity"
    private lateinit var binding: ActivityShowGroupCodeBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowGroupCodeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding.textGroupCode.text = userViewModel.getGroupId()
        binding.btnSignin.setOnClickListener {
            val intent = Intent(applicationContext, SigninActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}