package com.mirim.refrigerator.server.responses

import com.google.gson.annotations.SerializedName

data class SigninResponse (
    @SerializedName("status")
    var status : Int?,
    @SerializedName("message")
    var message : String?,
    @SerializedName("userId")
    var userId : String,
    @SerializedName("userName")
    var userName : String,
    @SerializedName("userNickname")
    var userNickname : String,
    @SerializedName("userEmail")
    var userEmail : String,
    @SerializedName("groupId")
    var groupId : String,
)