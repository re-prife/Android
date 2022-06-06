package com.mirim.refrigerator.network

import com.mirim.refrigerator.server.request.SigninRequest
import com.mirim.refrigerator.server.request.SignupRequest
import com.mirim.refrigerator.server.responses.SigninResponse
import com.mirim.refrigerator.server.responses.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceAPI {

    // 회원가입
    @POST("users")
    fun signup(
        @Body signUpInfo : SignupRequest
    ): Call<SignupResponse>

    // 로그인
    @POST("users/login")
    fun signin(
        @Body signInInfo: SigninRequest
    ):Call<SigninResponse>




}