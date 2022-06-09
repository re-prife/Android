package com.mirim.refrigerator.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirim.refrigerator.adapter.IngredientTypeAdapter
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.IngredientsResponse
import com.mirim.refrigerator.viewmodel.app
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class Ingredient(
    val ingredientCategory: String?,
    val ingredientCount : String?,
    val ingredientExpirationDate: String?,
    val ingredientMemo: String?,
    val ingredientName: String?,
    val ingredientPurchaseDate: String?,
    val ingredientSaveType: String?,
    val ingredientImageName: String?,
    val ingredientId: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(parcel: Parcel): Ingredient {
            return Ingredient(parcel)
        }

        override fun newArray(size: Int): Array<Ingredient?> {
            return arrayOfNulls(size)
        }
        fun typeKoreanConverter(english: String?) : String {
            return when(english) {
                "VEGGIE" -> "채소"
                "FRUIT" -> "과일"
                "SEA_FOOD" -> "수산물"
                "GRAIN" -> "곡물"
                "MEAT" -> "육류"
                "SEASONING" -> "양념"
                "BEVERAGE" -> "양념"
                "PROCESSED_FOOD" -> "가공식품"
                "SNACK" -> "간식"
                "DAIRY_PRODUCT" -> "유제품"
                "SIDE_DISH" -> "반찬"
                else -> "기타"
            }
        }
        fun typeEnglishConverter(korean: String?): String {
            return when(korean) {
                "채소" -> "VEGGIE"
                "과일" -> "FRUIT"
                "수산물" -> "SEA_FOOD"
                "곡물" -> "GRAIN"
                "육류" -> "MEAT"
                "양념" -> "SEASONING"
                "음료" -> "BEVERAGE"
                "가공식품" -> "PROCESSED_FOOD"
                "간식" -> "SNACK"
                "유제품" -> "DAIRY_PRODUCT"
                "반찬" -> "SIDE_DISH"
                else -> "ETC"
            }
        }
        fun storeKoreanConverter(english: String?) : String {
            return when(english) {
                "FRIDGE" -> "냉장"
                "FREEZER" -> "냉동"
                else -> "실온"
            }
        }
        fun storeEnglishConverter(korean: String?) : String {
            return when(korean) {
                "냉장" -> "FRIDGE"
                "냉동" -> "FREEZER"
                else -> "ROOM_TEMP"
            }
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ingredientCategory)
        parcel.writeString(ingredientCount)
        parcel.writeString(ingredientExpirationDate)
        parcel.writeString(ingredientMemo)
        parcel.writeString(ingredientName)
        parcel.writeString(ingredientPurchaseDate)
        parcel.writeString(ingredientSaveType)
        parcel.writeString(ingredientImageName)
        parcel.writeLong(ingredientId)
    }

    override fun describeContents(): Int {
        return 0
    }


}