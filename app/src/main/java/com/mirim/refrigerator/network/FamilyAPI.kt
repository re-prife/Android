package com.mirim.refrigerator.network

import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.model.Notice
import com.mirim.refrigerator.server.responses.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FamilyAPI {
    @GET("/groups/{groupId}/{userId}")
    fun getFamilyList(
        @Path("groupId") groupId: Int?,
        @Path("userId") userId: Int?
    ): Call<List<FamilyMember>>

    // 공지사항 조회
    @GET("/groups/{groupId}/report")
    fun getNotice(
        @Path("groupId") groupId: Int?
    ) : Call<Notice>

    // 공지사항 수정
    @PUT("/groups/{groupId}/report")
    fun updateNotice(
        @Path("groupId") groupId: Int?,
        @Body body: Notice
    ) : Call<Response>
}