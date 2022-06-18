package com.mirim.refrigerator.server.responses

data class ErrandDetailResponse (
    val acceptUserId : Int,
    val completeCheck : Boolean,
    val questCreatedDate : String,
    val questTitle : String,
    val requestUserId : Int?,
    val questContent : String
)