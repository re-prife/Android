package com.mirim.refrigerator.server.responses


data class JoinGroupResponse (
    var status : Int?,
    var message : String?,
    var groupId : Int,
    var groupInviteCode : String
)