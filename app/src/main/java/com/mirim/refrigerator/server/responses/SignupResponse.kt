package com.mirim.refrigerator.server.responses


data class SignupResponse (
    var status : Int,
    var message : String?,
    var userEmail : String,
    var userId : Int,
    var userName : String,
    var userNickname: String,
)