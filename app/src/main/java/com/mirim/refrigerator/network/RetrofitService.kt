package com.mirim.refrigerator.network

import com.mirim.refrigerator.server.NullOnEmptyConverter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    /*

    name : 테스트
    nickname : test
    email : testing@naver.com
    pw : 12345678

     */

    companion object {
        var serviceAPI : ServiceAPI
        val BASE_URL : String = "http://52.204.65.160:8080/"
        val retrofit : Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(NullOnEmptyConverter())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init {
            serviceAPI = retrofit.create(ServiceAPI::class.java)
        }
    }
}