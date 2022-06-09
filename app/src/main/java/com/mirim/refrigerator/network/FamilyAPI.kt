package com.mirim.refrigerator.network

import com.mirim.refrigerator.model.FamilyMember
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FamilyAPI {
    @GET("/groups/{groupId}/{userId}")
    fun getFamilyList(
        @Path("groupId") groupId: Int?,
        @Path("userId") userId: Int?
    ): Call<List<FamilyMember>>
}