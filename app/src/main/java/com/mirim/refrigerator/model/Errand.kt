package com.mirim.refrigerator.model

data class Errand(
    var acceptUserId : Int?,
    var completeCheck : Boolean,
    var questId : Int,
    var questTitle : String,
    var requestUserId : Int,
    var questCreatedDate : String?
)