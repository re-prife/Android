package com.mirim.refrigerator.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.model.User

class UserViewModel : ViewModel() {
    companion object {
        val TAG = "태그"
    }
    private val user = MutableLiveData<User>()
    private var family = ArrayList<FamilyMember>()

    fun getUser(): LiveData<User> {
        return user
    }
    fun getUserId() : Int? {
        return user.value?.userId
    }
    fun setGroupId(groupId : Int) {
        user.value?.groupId = groupId
    }
    fun getGroupId() : Int? {
        return user.value?.groupId
    }

    fun getFamily() : ArrayList<FamilyMember> {
        return family
    }
    fun getImage() : String? {
        return user.value?.userImagePath
    }


    // 가족 구성원 개인의 정보를 반환
    fun getFamilyMember(memberId: Int?) : FamilyMember? {
        for(member in family) {
            if(member.userId==memberId) {
                return member
            }
        }
        return null
    }
    // 가족 전체 정보를 담은 List 지정
    fun setFamilyList(familyList : List<FamilyMember>) {
        val newArr = ArrayList<FamilyMember>()
        for(lst in familyList) {
            newArr.add(FamilyMember(userNickname = lst.userNickname, userName = lst.userName, userId = lst.userId, userImagePath = lst.userImagePath))
        }
        family = newArr
    }

    init {
        Log.d(TAG, "MyNumberViewModel - 생성자 호출")
    }


     fun loadUsers(u: User) {
        user.value = u
    }
}