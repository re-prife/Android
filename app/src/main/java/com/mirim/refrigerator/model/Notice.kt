package com.mirim.refrigerator.model

import android.os.Bundle

class Notice(var title: String?, var contents: String?) {

    companion object {
        val notice = Notice("엄마의 잔소리", "세부 내용")
        fun getInstance() : Notice {
            return notice
        }
    }


}