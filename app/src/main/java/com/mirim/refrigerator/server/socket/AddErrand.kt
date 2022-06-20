package com.mirim.refrigerator.server.socket

import com.mirim.refrigerator.view.errand.AddErrandData

data class AddErrand (
    val userIds : List<Int>,
    val data : AddErrandData
)