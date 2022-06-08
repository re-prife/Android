package com.mirim.refrigerator.server.responses

class IngredientsResponse(
    var ingredientName: String,
    var ingredientSaveType: String,
    var ingredientExpirationDate: IntArray,
    var ingredientCategory: String,
    var ingredientCount: String,
    var ingredientImageName: String
) {

}