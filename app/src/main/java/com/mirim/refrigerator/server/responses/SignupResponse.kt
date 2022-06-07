package com.mirim.refrigerator.server.responses

import com.google.gson.annotations.SerializedName

data class SignupResponse (
    @SerializedName("status")
    var status : Int,
    @SerializedName("message")
    var message : String?
)