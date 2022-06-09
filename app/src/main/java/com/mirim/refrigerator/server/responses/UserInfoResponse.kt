package com.mirim.refrigerator.server.responses

import com.mirim.refrigerator.model.ChoreKing
import com.mirim.refrigerator.model.QuestKing

data class UserInfoResponse(
    val king : HomeKingsResponse,
    val userEmail : String,
    val userImagePath : String,
    val userName : String,
    val userNickname : String
)