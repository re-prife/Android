package com.mirim.refrigerator.network

import com.mirim.refrigerator.server.responses.HomeKingsResponse
import com.mirim.refrigerator.server.responses.UserInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAPI {
    // 회원 상세 정보 조회
    @GET("users/{userId}")
    fun getUserData(
        @Path("userId") groupId : Int,
        @Query("date") date : String
    ): Call<UserInfoResponse>

}