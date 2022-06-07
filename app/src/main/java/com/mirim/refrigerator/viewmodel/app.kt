package com.mirim.refrigerator.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.model.User

class app : Application() {

    companion object {
        lateinit var user : User
    }


}