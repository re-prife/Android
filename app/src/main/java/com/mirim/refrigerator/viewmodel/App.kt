package com.mirim.refrigerator.viewmodel

import android.app.Application
import com.mirim.refrigerator.model.User

object App {

    lateinit var user : User

    override fun toString(): String {
        val value : String = "이름"+user.name+"\n이메일 : "+user.email+"\n그룹 : "+user.groupId
        return value
    }

}