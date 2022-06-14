package com.mirim.refrigerator.server.request

data class ModifyUserPasswordRequest (
    val userNewPassword : String,
    val userNewPasswordCheck : String,
    val userPassword : String
)