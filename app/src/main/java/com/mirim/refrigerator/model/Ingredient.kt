package com.mirim.refrigerator.model

data class Ingredient(
    val ingredientCategory: String,
    val ingredientCount : String,
    val ingredientExpirationDate: String,
    val ingredientMemo: String,
    val ingredientName: String,
    val ingredientPurchaseDate: String,
    val ingredientSaveType: String
)