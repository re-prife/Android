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

    fun loadErrand(
        acceptUserId : Int?,
        completeCheck : Boolean,
        questId : Int,
        questTitle : String,
        requestUserId : Int,
        questCreatedDate : String?
    ) {
        errand.value?.acceptUserId = acceptUserId
        errand.value?.completeCheck = completeCheck
        errand.value?.questId = questId
        errand.value?.questTitle = questTitle
        errand.value?.requestUserId = requestUserId
        errand.value?.questCreatedDate = questCreatedDate
    }



}