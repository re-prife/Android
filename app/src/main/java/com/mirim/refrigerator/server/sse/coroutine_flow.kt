package com.mirim.refrigerator.server.sse

import android.util.Log
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.viewmodel.App
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import tylerjroach.com.eventsource_android.EventSourceHandler
import tylerjroach.com.eventsource_android.MessageEvent
import java.io.BufferedReader
import java.io.InputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

data class Event(val userNickname : String = "no value", val title : String = "no value")


fun getEventsFlow(): kotlinx.coroutines.flow.Flow<Event> = flow {

    var inputStream : InputStream? = null
    var inputReader : BufferedReader? = null
    var event : Event = Event()
    coroutineScope {
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
                        event = event.copy(title = line.substring(5).trim())
                    }
                    line.isEmpty() -> {
                        emit(event)
                        event = Event()
                    }
                }
            }
        }
    }
}




class webSocket : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response?) {
        webSocket.send("{\"type\":\"ticker\", \"symbols\": [\"BTC_KRW\"], \"tickTypes\": [\"30M\"]}")
        webSocket.close(NORMAL_CLOSURE_STATUS, null) //없을 경우 끊임없이 서버와 통신함
    }

    override fun onMessage(webSocket: WebSocket?, text: String) {
        Log.d("Socket","Receiving : $text")
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
        Log.d("Socket", "Receiving bytes : $bytes")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Socket","Closing : $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable, response: Response?) {
        Log.d("Socket","Error : " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}


class SSEHandler : EventSourceHandler {
    override fun onConnect() {
        Log.d("SSE Connnected", "TRUE")
    }

    override fun onMessage(p0: String?, p1: MessageEvent?) {
        Log.d("SSE Message", p0!!)
        Log.d("SSE Message : ", p1!!.lastEventId)
        Log.d("SSE Message : ", p1!!.data)
    }

    override fun onComment(p0: String?) {
        Log.d("SSE Comment", p0!!)
    }

    override fun onError(p0: Throwable?) {
        Log.d("SSE Error", "TRUE")
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        p0?.printStackTrace(pw)
        Log.d("SSE Stacktrace", sw.toString())
    }

    override fun onClosed(p0: Boolean) {
        Log.d("SSE Closed", "reconnect ? " + p0)
    }
}