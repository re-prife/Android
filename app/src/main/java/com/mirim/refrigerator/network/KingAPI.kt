package com.mirim.refrigerator.network

import com.mirim.refrigerator.server.responses.HomeKingsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KingAPI {
    // 이달의 왕 조회
    @GET("groups/{groupId}/kings")
    fun getMonthKing(
        @Path("groupId") groupId : Int,
        @Query("date") date : String
    ): Call<HomeKingsResponse>




}