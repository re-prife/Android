package com.mirim.refrigerator.model

import java.io.Serializable

/*
 *  "ingredientName" : "salad source",
 *	"ingredientSaveType" : "냉동", //저장 방식 냉장, 냉동 등등
 *	"ingredientPurchaseDate" : "2022-02-02", //구매 날짜
 *	"ingredientExpirationDate" : "2023-02-02", //유통기한
 *	"ingredientCategory" : "source", //음식 종류 소스, 야채, 고기 등등
 *	"ingredientCount " : "1개",
 *	"ingredientMemo" : "메모메모"
 */

/* NULLABLE의 기준은 사용자가 값을 직접 입력 및 수정해야 하는 경우로 함 */
data class Barcode (
    var ingredientName : String,
    var ingredientSaveType : String,
    var ingredientPurchaseDate : String?,
    var ingredientExpirationDate : String,
    var ingredientCategory : String?,
    var ingredientCount : String,
    var ingredientMemo : String?
) :Serializable