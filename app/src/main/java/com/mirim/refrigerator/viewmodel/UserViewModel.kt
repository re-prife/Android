package com.mirim.refrigerator.viewmodel

import android.os.AsyncTask
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
    private var family = ArrayList<User>()

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

    fun getFamily() : ArrayList<User> {
        return family
    }


    // 가족 구성원 개인의 정보를 반환
    fun getFamilyMember(memberId : Int) : User? {
        for(member in family) {
            if(member.userId==memberId) {
                return member
            }
        }
        Log.d(TAG,"UserViewModel - 가족 멤버가 존재하지 않음")
        return null
    }
    // 가족 전체 정보를 담은 List 지정
    fun setFamilyList(familyList : List<FamilyMember>) {
        val newArr = ArrayList<User>()
        for(lst in familyList) {
            newArr.add(User(lst.userNickname,lst.userName,null,lst.userId,getGroupId(),lst.userImagePath))
        }
        family = newArr
    }

    init {
        Log.d(TAG, "MyNumberViewModel - 생성자 호출")
    }


     fun loadUsers(u: User) {
        // Do an asynchronous operation to fetch users.
        user.value = u
    }
}