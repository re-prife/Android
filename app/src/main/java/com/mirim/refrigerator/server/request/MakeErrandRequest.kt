package com.mirim.refrigerator.server.request

data class MakeErrandRequest (
    val questContent : String,
    val questTitle : String,
    val userIds : List<Int>?
)