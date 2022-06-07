package com.mirim.refrigerator.network

import com.mirim.refrigerator.server.request.CreateGroupRequest
import com.mirim.refrigerator.server.request.JoinGroupRequest
import com.mirim.refrigerator.server.request.SigninRequest
import com.mirim.refrigerator.server.request.SignupRequest
import com.mirim.refrigerator.server.responses.CreateGroupResponse
import com.mirim.refrigerator.server.responses.JoinGroupResponse
import com.mirim.refrigerator.server.responses.SigninResponse
import com.mirim.refrigerator.server.responses.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

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

    // 그룹생성
    @POST("/groups/{userId}")
    fun createGroup(
        @Path("userId") userId : String,
        @Body groupInfo : CreateGroupRequest
    ):Call<CreateGroupResponse>

    // 그룹가입
    @POST("/groups/join/{userId}")
    fun joinGroup(
        @Path("userId") userId: String,
        @Body groupCode : JoinGroupRequest
    ):Call<JoinGroupResponse>


}