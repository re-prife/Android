package com.mirim.refrigerator.view.housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.google.gson.Gson
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityCertifyChoreBinding
import com.mirim.refrigerator.model.Housework
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.network.SocketHandler
import com.mirim.refrigerator.server.responses.ChoreReactionResponse
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.server.socket.AcceptChore
import com.mirim.refrigerator.server.socket.AcceptChoreData
import com.mirim.refrigerator.viewmodel.App
import io.socket.client.Socket
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class CertifyChoreActivity : AppCompatActivity() {
    lateinit var binding: ActivityCertifyChoreBinding
    lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertifyChoreBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.root)

        socket = SocketHandler.getterSocket()

        val title = intent.getStringExtra("title")
        val category = intent.getStringExtra("category")
        val nickname = intent.getStringExtra("nickname")
        val choreId = intent.getIntExtra("choreId", -1)

        binding.txtHouseworkCategory.text = Housework.categoryKoreanConverter(category)
        binding.txtHouseworkTitle.text = nickname + "님이 " + title + "을 완료했습니다."

        binding.btnApprove.setOnClickListener {
            RetrofitService.houseworkAPI.reactionChore(App.user.groupId, choreId = choreId).enqueue(object : Callback<ChoreReactionResponse> {
                override fun onResponse(
                    call: Call<ChoreReactionResponse>,
                    response: retrofit2.Response<ChoreReactionResponse>
                ) {
                    Log.d("CertifyChoreActivity", response.body().toString())
                    if(response.raw().code() == 200) {
                        val data = AcceptChoreData(response.body()?.requesterId, AcceptChore(category, title))
                        socket.emit("acceptChore",
                            JSONObject(
                                Gson().toJson(data)
                            )
                        )
                        Toast.makeText(applicationContext, "승인되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<ChoreReactionResponse>, t: Throwable) {
                    Log.d("CertifyChoreActivity-fail", t.toString())
                }

            })
        }

        binding.btnDismiss.setOnClickListener {
            Toast.makeText(applicationContext, "거절되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}