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
    fun getUserId() : Int? {
        return user.value?.userId
    }
    fun setGroupId(groupId : Int) {
        user.value?.groupId = groupId
    }
    fun getGroupId() : Int? {
        return user.value?.groupId
    }

    init {
        Log.d(TAG, "MyNumberViewModel - 생성자 호출")
    }


     fun loadUsers(u: User) {
        // Do an asynchronous operation to fetch users
        user.value = u
    }
}