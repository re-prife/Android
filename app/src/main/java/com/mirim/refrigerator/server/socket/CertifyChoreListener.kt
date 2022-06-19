package com.mirim.refrigerator.server.socket

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.mirim.refrigerator.R
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject

class CertifyChoreListener(val context: Context): Emitter.Listener {
    companion object {
        val TAG = "CertifyChoreData"
    }
    override fun call(vararg args: Any?) {
        val data: JSONObject = args[0] as JSONObject
        Log.d("mySocket", data.toString())
        var certifyChore = Gson().fromJson(data.toString(), CertifyChore::class.java)

        try {
            Log.d("mySocket", certifyChore.title!!)
            var builder = NotificationCompat.Builder(context, "0")
                .setSmallIcon(R.drawable.icon_camera)
                .setContentTitle(certifyChore.title)
                .setContentText(certifyChore.userNickname)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(0, builder.build())
            }
        } catch (e: JSONException) {
            return;
        }
    }
}