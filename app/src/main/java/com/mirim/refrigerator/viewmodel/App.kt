package com.mirim.refrigerator.viewmodel

import android.app.Application
import android.net.Uri
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.model.User

object App {

    lateinit var user : User
    lateinit var groupInviteCode : String
    lateinit var family : List<FamilyMember>
    var imageUri: Uri? = null

    override fun toString(): String {
        return  "\n이름 : "+user.name+
                "\n닉네임 : "+user.nickname+
                "\n이메일 : "+user.email+
                "\n그룹 : "+ user.groupId+
                "\n이미지 : "+user.userImagePath+
                "\n가족 : "+ family.size
    }

}