package com.mirim.refrigerator.network

import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.server.request.CreateIngredientRequest
import com.mirim.refrigerator.server.request.DeleteIngredientsRequest
import com.mirim.refrigerator.server.request.UpdateMultiIngredientRequest
import com.mirim.refrigerator.server.responses.CreateIngredientResponse
import com.mirim.refrigerator.server.responses.DeleteIngredientsResponse
import com.mirim.refrigerator.server.responses.Response
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface IngredientAPI {
    // 식재료 조회
    @GET("/groups/{groupId}/ingredients")
    fun getIngredients(
        @Path("groupId") groupId: Int?
    ): Call<List<Ingredient>>
    @GET("/groups/{groupId}/ingredients")
    fun getIngredients(
        @Path("groupId") groupId: Int?,
        @Query("saveType") saveType: String
    ): Call<List<Ingredient>>

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

    // 식재료 삭제
    @HTTP(method = "DELETE", path = "groups/{groupId}/ingredients", hasBody = true)
    fun deleteIngredients(
        @Path("groupId") groupId: Int?,
        @Body ingredients: List<DeleteIngredientsRequest>
    ) : Call<DeleteIngredientsResponse>

    // 식재료 이미지 변경
    @Multipart
    @POST("/uploads/ingredients/{ingredientId}")
    fun uploadIngredientImage(
        @Path("ingredientId") ingredientId: Long?,
        @Part file: MultipartBody.Part
    ) : Call<Response>

    // 식재료 일괄 수정
    @PUT("/groups/{groupId}/ingredients")
    fun updateMultiIngredients(
        @Path("groupId") groupId: Int?,
        @Body body : List<UpdateMultiIngredientRequest>
    ) : Call<Response>
}