package com.mirim.refrigerator.server.socket

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.mirim.refrigerator.network.SocketHandler
import com.mirim.refrigerator.view.errand.*
import com.mirim.refrigerator.view.housework.AcceptChoreActivity
import com.mirim.refrigerator.view.housework.CertifyChoreActivity
import io.socket.client.Socket
import org.json.JSONObject
import java.lang.Exception

class SocketService : Service() {
    companion object {
        val CERTIFY_CHORE = "집안일 인증 요청 도착"
        val ACTION_CERTIFY_CHORE = "com.mirim.refrigerator.ACTION_CERTIFY_CHORE"
    }

    lateinit var socket: Socket
    lateinit var certifyChoreActivity: PendingIntent
    lateinit var certifyChorePopup: Intent
  
    lateinit var acceptChoreActivity: PendingIntent
    lateinit var acceptChorePopup: Intent

    lateinit var addErrandActivity : PendingIntent
    lateinit var addErrandPopup : Intent

    lateinit var acceptErrandPopup : Intent
    lateinit var acceptErrandActivity : PendingIntent


    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("myService", "서비스 시작됨")
        certifyChorePopup = Intent(applicationContext, CertifyChoreActivity::class.java)
        acceptChorePopup = Intent(applicationContext, AcceptChoreActivity::class.java)

        socket = SocketHandler.getterSocket()
        // socket.on("certifyChore", CertifyChoreListener(applicationContext))
        socket.on("certifyChore") { args ->
            val data: JSONObject = args[0] as JSONObject
            Log.d("mySocket", data.toString())
            var certifyChore = Gson().fromJson(data.toString(), CertifyChore::class.java)
            certifyChorePopup.putExtra("category", certifyChore.category)
            certifyChorePopup.putExtra("title", certifyChore.title)
            certifyChorePopup.putExtra("nickname", certifyChore.userNickname)
            certifyChorePopup.putExtra("choreId", certifyChore.choreId)
            certifyChoreActivity = PendingIntent.getActivity(applicationContext, 0, certifyChorePopup, PendingIntent.FLAG_ONE_SHOT)
            try {
                Log.d("mySocket", certifyChore.title!!)
                certifyChoreActivity.send()
            } catch (e: Exception) {
                Log.d("mySocket", e.toString())
            }
        }

        socket.on("acceptChore") { args ->
            val data: JSONObject = args[0] as JSONObject
            Log.d("mySocket-accept", data.toString())
            // var requesterId = JsonParser().parse(Gson().toJson(data))
            var acceptChore = Gson().fromJson<AcceptChore>(data.toString(), AcceptChore::class.java)

            acceptChorePopup.putExtra("category", acceptChore.category)
            acceptChorePopup.putExtra("title", acceptChore.title)
            acceptChoreActivity = PendingIntent.getActivity(applicationContext, 1, acceptChorePopup, PendingIntent.FLAG_ONE_SHOT)

            try {
                acceptChoreActivity.send()
            } catch (e: Exception) {
                Log.d("mySocket", e.toString())
            }
        }

        // 심부름
        addErrandPopup = Intent(applicationContext, AddErrandActivity::class.java)
        socket.on("addQuest") { args ->
            Log.e("SocketService","addQuest 호출")
            val data : JSONObject = args[0] as JSONObject
            val addQuest = Gson().fromJson(data.toString(),AddErrandData::class.java)
            addErrandPopup.putExtra("title",addQuest.title)
            addErrandPopup.putExtra("nickname",addQuest.userNickname)
            addErrandPopup.putExtra("requester",addQuest.requesterId)
            addErrandActivity = PendingIntent.getActivity(applicationContext,2,addErrandPopup,PendingIntent.FLAG_ONE_SHOT)
            try {
                addErrandActivity.send()
            } catch (e : Exception) {
                Log.e("SSSSSSSSSSSS","Socket Service addQuest 예외 발생")
            }
        }

        acceptErrandPopup = Intent(applicationContext, AcceptErrandActivity::class.java)
        socket.on("acceptQuest") { args ->
            Log.e("SocketService","acceptQuest 호출")
            val data : JSONObject = args[0] as JSONObject
            val acceptErrand = Gson().fromJson(data.toString(),AcceptErrandData::class.java)
            acceptErrandPopup.putExtra("title",acceptErrand.title)
            acceptErrandPopup.putExtra("userNickname",acceptErrand.userNickname)
            acceptErrandActivity = PendingIntent.getActivity(applicationContext,3,acceptErrandPopup,PendingIntent.FLAG_ONE_SHOT)
            try {
                acceptErrandActivity.send()
            } catch (e : Exception) {
                Log.e("SSSSSSSSSSSS","Socket Service acceptQuest 예외 발생")
            }
        }


        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}