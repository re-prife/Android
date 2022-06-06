package com.mirim.refrigerator.server.request

data class SigninRequest (
    var userEmail: String,
    var userPassword: String
)