package com.mirim.refrigerator.server.socket

class SaveInfo(
    val userId: Int?,
    val groupId: Int?
) {
    override fun toString(): String {
        return """
            {
                "userId":${userId},
                "groupId":${groupId}
            }
        """.trimIndent()
    }
}