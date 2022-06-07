package com.mirim.refrigerator.viewmodel

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirim.refrigerator.model.User

class UserViewModel : ViewModel() {
    companion object {
        val TAG = "태그"
    }
    private val user = MutableLiveData<User>()

    fun getUser(): LiveData<User> {
        return user
    }
    fun getUserId() : String? {
        return user.value?.userId
    }

    init {
        Log.d(TAG, "MyNumberViewModel - 생성자 호출")
        user.value = User("닉네임", "이름", "이메일","1","1")
    }


     fun loadUsers(u: User) {
        // Do an asynchronous operation to fetch users.
        user.value?.nickname=u.nickname
        user.value?.name=u.name
        user.value?.email=u.email
        user.value?.groupId = u.groupId
        user.value?.userId = u.userId
    }
}