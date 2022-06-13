package com.mirim.refrigerator.network

import com.mirim.refrigerator.server.request.ModifyUserInfoRequest
import com.mirim.refrigerator.server.request.ModifyUserPasswordRequest
import com.mirim.refrigerator.server.responses.HomeKingsResponse
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.server.responses.UserInfoResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {
    // 회원 상세 정보 조회
    @GET("users/{userId}")
    fun getUserData(
        @Path("userId") uesrId : Int,
        @Query("date") date : String
    ): Call<UserInfoResponse>

    // 프로필 수정
    @PUT("users/{userId}")
    fun modifyUser(
        @Path("userId") userId : Int,
        @Body body : ModifyUserInfoRequest,
    ): Call<Response>

    // 프로핗 이미지 수정
    @POST("uploads/users/{userId}")
    fun modifyUserImage(
        @Path("userId") userId : Int,
        @Body profileImage : MultipartBody.Part
    ): Call<Response>

    // 사용자 비밀번호 변경
    @PUT("users/{userId}/password")
    fun modifyUserPassword(
        @Path("userId") userId: Int,
        @Body body : ModifyUserPasswordRequest,
    ): Call<Response>

}