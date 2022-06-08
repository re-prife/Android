package com.mirim.refrigerator.server.request

class CreateIngredientRequest(
    var ingredientCategory: String?,
    var ingredientCount: String?,
    var ingredientExpirationDate: String?,
    var ingredientMemo: String?,
    var ingredientName: String?,
    var ingredientPurchaseDate: String?,
    var ingredientSaveType: String?,
) {
}