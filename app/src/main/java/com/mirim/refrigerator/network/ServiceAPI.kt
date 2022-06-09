package com.mirim.refrigerator.network

import com.mirim.refrigerator.model.Housework
import com.mirim.refrigerator.server.request.*
import com.mirim.refrigerator.server.responses.*
import retrofit2.Call
import retrofit2.http.*

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

    // 식재료 조회
    @GET("/groups/{groupId}/ingredients")
    fun getIngredients(
        @Path("groupId") groupId: Int?
    ):Call<List<IngredientsResponse>>
    @GET("/groups/{groupId}/ingredients")
    fun getIngredients(
        @Path("groupId") groupId: Int?,
        @Query("saveType") saveType: String
    ):Call<List<IngredientsResponse>>

    // 식재료 생성
    @POST("/groups/{groupId}/ingredients")
    fun createIngredients(
        @Path("groupId") groupId: Int?,
        @Body ingredient: CreateIngredientRequest
    ): Call<CreateIngredientResponse>

    // 식재료 수정
    @PUT("groups/{groupId}/ingredients/{ingredientId}")
    fun updateIngredients(
        @Path("groupId") groupId: Int?,
        @Path("ingredientId") ingredientId: Long?,
        @Body ingredient: CreateIngredientRequest
    ): Call<CreateIngredientResponse>

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

    // 식재료 삭제
    @DELETE("groups/{groupId}/ingredients")
    fun deleteIngredients(
        @Path("groupId") groupId: Int?,
        @Body ingredients: List<DeleteIngredientsRequest>
    ) : Call<DeleteIngredientsResponse>

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

}