package com.mirim.refrigerator.network

import com.mirim.refrigerator.server.request.MakeErrandRequest
import com.mirim.refrigerator.server.responses.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ErrandAPI {
    // 심부름 생성
    @POST("groups/{groupId}/quests/{userId}")
    fun makeErrand(
        @Path("groupId") groupId : Int,
        @Path("userId") userId : Int,
        @Body errand : MakeErrandRequest
    ): Call<Response>
}