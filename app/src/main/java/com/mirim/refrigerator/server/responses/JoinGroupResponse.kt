package com.mirim.refrigerator.server.responses

import com.google.gson.annotations.SerializedName

data class JoinGroupResponse (
    @SerializedName("status")
    var status : Int?,
    @SerializedName("message")
    var message : String?,
)