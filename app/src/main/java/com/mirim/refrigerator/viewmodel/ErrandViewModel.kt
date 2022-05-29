package com.mirim.refrigerator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirim.refrigerator.model.Errand
import java.util.*

class ErrandViewModel : ViewModel() {
    companion object {
        val TAG = "태그"
    }
    private val errand = MutableLiveData<Errand>()

    fun getErrand() : LiveData<Errand> {
        return errand
    }

    init {
        errand
    }

    fun loadErrand( title : String, content : String, accepter : String?, requester : String?, date : Date?){
        errand.value?.title = title
        errand.value?.content = content
        errand.value?.date = date
        errand.value?.accepter = accepter
        errand.value?.requester = requester
    }



}