package com.mirim.refrigerator.view.errand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.google.gson.Gson
import com.mirim.refrigerator.R
import com.mirim.refrigerator.adapter.ErrandListAdapter
import com.mirim.refrigerator.databinding.ActivityAddErrandBinding
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.network.SocketHandler
import com.mirim.refrigerator.network.SocketHandler.socket
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.viewmodel.App
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

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
        val questId = intent.getIntExtra("questId",-1)

        Log.e("WWWWWW",requesterId.toString())

        binding.txtTitle.text = title
        binding.txtRequester.text = "from. $nickname"

        binding.btnAccept.setOnClickListener {
            RetrofitService.errandAPI.acceptOrCancel(App.user.groupId,questId,App.user.userId)
                .enqueue(object : Callback<Response> {
                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        val raw = response.raw()
                        when(raw.code()) {
                            200 -> {
                                Toast.makeText(applicationContext,"심부름을 수락하였습니다.", Toast.LENGTH_SHORT).show()
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
                            }
                            404 -> {
                                Toast.makeText(applicationContext,"잘못된 심부름 정보입니다.", Toast.LENGTH_SHORT).show()
                            }
                            409 -> {
                                Toast.makeText(applicationContext,"이미 해결되거나 수락자가 존재하는 심부름입니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<Response>,
                        t: Throwable
                    ) {
                        Toast.makeText(applicationContext,"심부름에 접근할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                })
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