package com.mirim.refrigerator.server.request

data class SignupRequest (
    var userEmail : String,
    var userName : String,
    var userNickname : String,
    var userPassword : String
)