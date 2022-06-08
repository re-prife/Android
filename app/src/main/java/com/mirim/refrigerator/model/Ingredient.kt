package com.mirim.refrigerator.model

class Ingredient(
    val ingredientCategory: String,
    val ingredientCount : String,
    val ingredientExpirationDate: IntArray,
    val ingredientMemo: String,
    val ingredientName: String,
    val ingredientPurchaseDate: String,
    val ingredientSaveType: String,
    val ingredientImageName: String?,
) {
    companion object {
        fun typeKoreanConverter(english: String) : String {
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
    }
}