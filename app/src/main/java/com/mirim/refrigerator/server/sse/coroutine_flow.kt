package com.mirim.refrigerator.server.sse

import android.util.Log
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.viewmodel.App
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

data class Event(val userNickname : String = "no value", val questTitle : String = "no value")


fun getEventsFlow(): kotlinx.coroutines.flow.Flow<Event> = flow {

    var inputStream : InputStream? = null
    var inputReader : BufferedReader? = null
    var event : Event = Event()

    try {
        val url : String = RetrofitService.CONNECT_SSE+"subscribe/${App.user.userId}"
        val conn = (URL(url).openConnection() as HttpURLConnection).also {
            it.requestMethod = "GET"
            it.setRequestProperty("Accept","text/event-stream")
            it.doInput = true
        }

        conn.connect()

        val response = conn.responseCode
        Log.d("AAAAAAAAAA","conn.code : "+conn.responseCode)
        if(response == 200) {
            inputReader = conn.inputStream.bufferedReader()
            event = Event()
        }


    } catch (e : Exception) {
        Log.e("croutine_flow",e.message.toString())
    }



    // run forever
    while(true) {
        val line = inputReader?.readLine()

        if (line != null) {
            when {
                line.startsWith("event:") -> {
                    event = event.copy(userNickname = line.substring(6).trim())
                }
                line.startsWith("data:") -> {
                    event = event.copy(questTitle = line.substring(5).trim())
                }
                line.isEmpty() -> {
                    emit(event)
                    event = Event()
                }
            }
        }
    }



}