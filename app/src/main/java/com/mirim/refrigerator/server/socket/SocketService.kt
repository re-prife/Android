package com.mirim.refrigerator.server.socket

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import com.mirim.refrigerator.network.SocketHandler
import com.mirim.refrigerator.view.housework.CertifyChoreActivity
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
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
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("myService", "서비스 시작됨")
        certifyChorePopup = Intent(applicationContext, CertifyChoreActivity::class.java)
        socket = SocketHandler.getterSocket()
        // socket.on("certifyChore", CertifyChoreListener(applicationContext))
        socket.on("certifyChore") { args ->
            val data: JSONObject = args[0] as JSONObject
            Log.d("mySocket", data.toString())
            var certifyChore = Gson().fromJson(data.toString(), CertifyChore::class.java)
            certifyChorePopup.putExtra("title", certifyChore.title)
            certifyChorePopup.putExtra("category", certifyChore.category)
            certifyChorePopup.putExtra("nickname", certifyChore.userNickname)
            certifyChoreActivity = PendingIntent.getActivity(applicationContext, 0, certifyChorePopup, PendingIntent.FLAG_ONE_SHOT)
            try {
                Log.d("mySocket", certifyChore.title!!)
                certifyChoreActivity.send()
            } catch (e: Exception) {
                Log.d("myService", e.toString())
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}