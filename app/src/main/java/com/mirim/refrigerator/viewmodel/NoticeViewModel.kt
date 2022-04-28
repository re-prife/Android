package com.mirim.refrigerator.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirim.refrigerator.model.Notice

class NoticeViewModel : ViewModel() {
    companion object {
        val TAG = "태그"
    }
    private val notice = MutableLiveData<Notice>()

    fun getNotice(): LiveData<Notice> {
        return notice
    }

    init {
        notice.value = Notice("하진아 청소해라", "오늘 청소 안하면 호적 파인다.")
    }

    fun loadNotice( title:String,  content:String) {
        // Do an asynchronous operation to fetch users.
        notice.value?.title=title
        notice.value?.contents=content

    }
}