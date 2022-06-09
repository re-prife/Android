package com.mirim.refrigerator.server.request

class CreateHouseworkRequest(
    var choreCategory: String?,
    var choreDate: String?,
    var choreTitle: String?,
    var choreUserId: Int?
) {
}