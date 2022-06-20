package com.mirim.refrigerator.network

import com.mirim.refrigerator.model.Errand
import com.mirim.refrigerator.server.request.MakeErrandRequest
import com.mirim.refrigerator.server.responses.ErrandDetailResponse
import com.mirim.refrigerator.server.responses.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface ErrandAPI {
    // 심부름 생성
    @POST("groups/{groupId}/quests/{userId}")
    fun makeErrand(
        @Path("groupId") groupId : Int,
        @Path("userId") userId : Int,
        @Body errand : MakeErrandRequest
    ): Call<Response>

    // 심부름 조회
    @GET("groups/{groupId}/quests")
    fun getErrandList(
        @Path("groupId") groupId: Int
    ) : Call<List<Errand>>

    // 심부름 상세 조회
    @GET("groups/{groupId}/quests/{questId}")
    fun getErrandDetail(
        @Path("groupId") groupId: Int?,
        @Path("questId") questId: Int
    ): Call<ErrandDetailResponse>

    // 심부름 수락 및 수락 후 취소
    @PUT("groups/{groupId}/quests/{questId}/acceptor/{acceptorId}")
    fun acceptOrCancel(
        @Path("groupId") groupId: Int?,
        @Path("questId") questId: Int?,
        @Path("acceptorId") acceptorId : Int?
    ): Call<Response>


    // 심부름 완료
    @PUT("groups/{groupId}/quests/{questId}/complete/{requesterId}")
    fun complete(
        @Path("groupId") groupId: Int?,
        @Path("questId") questId: Int?,
        @Path("requesterId") requesterId : Int?
    ): Call<Response>


    // 심부름 삭제
    @DELETE("groups/{groupId}/quests/{questId}")
    fun deleteErrand(
        @Path("groupId") groupId: Int?,
        @Path("questId") questId: Int?,
        @Query("userId") userId : Int?
    ): Call<Response>

    // 심부름 갱신
    @PUT("groups/{groupId}/quests/{questId}")
    fun updateErrand(
        @Path("groupId") groupId: Int?,
        @Path("questId") questId: Int?,
        @Query("requesterId") requesterId : Int?,
        @Body errand: MakeErrandRequest
    ): Call<Response>
}