package com.mirim.refrigerator.server.responses


data class SigninResponse (
    var status : Int?,
    var message : String?,
    var userId : Int,
    var userName : String,
    var userNickname : String,
    var userEmail : String,
    var groupId : Int,
)