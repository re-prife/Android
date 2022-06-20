package com.mirim.refrigerator.view.errand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.google.gson.Gson
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityAddErrandBinding
import com.mirim.refrigerator.databinding.ActivityCertifyChoreBinding
import com.mirim.refrigerator.network.SocketHandler
import com.mirim.refrigerator.network.SocketHandler.socket
import com.mirim.refrigerator.viewmodel.App
import org.json.JSONObject

class AddErrandActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddErrandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddErrandBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val nickname = intent.getStringExtra("nickname")
        val requesterId = intent.getIntExtra("requester",-1)

        Log.e("WWWWWW",requesterId.toString())

        binding.txtTitle.text = title
        binding.txtRequester.text = "from. $nickname"

        binding.btnAccept.setOnClickListener {
            // 수락
            socket = SocketHandler.getterSocket()
            val data = AcceptErrand(
                requesterId = requesterId,
                data = AcceptErrandData(
                    userNickname = App.user.nickname!!,
                    title = title!!
                )
            )
            socket.emit("acceptQuest",
                JSONObject (
                    Gson().toJson(data)
                )
            )
            finish()
            overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
        }
        binding.btnIgnore.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.translate_none,R.anim.translate_none)
        }

    }
}

data class AcceptErrand(
    val requesterId : Int,
    val data : AcceptErrandData
)

data class AcceptErrandData(
    val userNickname : String,
    val title : String
)