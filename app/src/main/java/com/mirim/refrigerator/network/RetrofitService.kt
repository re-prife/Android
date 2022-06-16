package com.mirim.refrigerator.network

import com.mirim.refrigerator.server.NullOnEmptyConverter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitService {

    /*

    name : 테스트
    nickname : test
    email : testing@naver.com
    pw : 12345678

     */

    companion object {
        var serviceAPI : ServiceAPI
        var familyAPI : FamilyAPI
        var kingAPI : KingAPI
        var userAPI : UserAPI
        var ingredientAPI : IngredientAPI
        var errandAPI : ErrandAPI
        var houseworkAPI: HouseworkAPI

        val BASE_URL : String = "http://52.204.65.160:8080/"
        val IMAGE_BASE_URL : String = "http://52.204.65.160:8080"
        val retrofit : Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(NullOnEmptyConverter())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init {
            serviceAPI = retrofit.create(ServiceAPI::class.java)
            familyAPI = retrofit.create(FamilyAPI::class.java)
            kingAPI = retrofit.create(KingAPI::class.java)
            userAPI = retrofit.create(UserAPI::class.java)
            ingredientAPI = retrofit.create(IngredientAPI::class.java)
            errandAPI = retrofit.create(ErrandAPI::class.java)
            houseworkAPI = retrofit.create(HouseworkAPI::class.java)
        }

    }
}