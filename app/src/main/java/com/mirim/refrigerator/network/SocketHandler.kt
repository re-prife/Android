package com.mirim.refrigerator.network
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException


object SocketHandler {
    lateinit var socket: Socket

    @Synchronized
    fun setSocket() {
        try {
            socket = IO.socket("http://52.204.65.160:3000/")
        } catch (e : URISyntaxException) {
            Log.d("mySocket", e.toString())
        }
    }

    @Synchronized
    fun getterSocket() : Socket {
        return socket
    }


    @Synchronized
    fun establishConnection() {
        socket.connect()
    }

    @Synchronized
    fun closeConnection() {
        socket.disconnect()
    }


}