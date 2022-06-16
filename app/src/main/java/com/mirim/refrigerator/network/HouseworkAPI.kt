package com.mirim.refrigerator.network

import com.mirim.refrigerator.model.Housework
import com.mirim.refrigerator.server.request.CreateHouseworkRequest
import com.mirim.refrigerator.server.responses.HouseworkResponse
import com.mirim.refrigerator.server.responses.Response
import retrofit2.Call
import retrofit2.http.*

interface HouseworkAPI {
    // 집안일 전체 조회
    @GET("/groups/{groupId}/chores")
    fun getChores(
        @Path("groupId") groupId: Int?,
        @Query("date") date: String?
    ) : Call<HouseworkResponse>

    // 집안일 하루 조회
    @GET("/groups/{groupId}/chores/one-day")
    fun getChoresOneDay(
        @Path("groupId") groupId: Int?,
        @Query("date") date: String?
    ) : Call<List<Housework>>

    // 집안일 생성
    @POST("groups/{groupId}/chores")
    fun createChore(
        @Path("groupId") groupId: Int?,
        @Body chore: CreateHouseworkRequest
    ) : Call<Response>

    // 집안일 삭제
    @DELETE("groups/{groupId}/chores/{choreId}")
    fun deleteChore(
        @Path("groupId") groupId: Int?,
        @Path("choreId") choreId: Int?
    ) : Call<Response>

    // 집안일 인증 요청
    @PUT("/groups/{groupId}/chores/{choreId}/certify")
    fun certifyChore(
        @Path("groupId") groupId: Int?,
        @Path("choreId") choreId: Int?
    ) : Call<Response>
}