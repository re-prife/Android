package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mirim.refrigerator.R
import com.mirim.refrigerator.view.errand.CreateErrandActivity
import com.mirim.refrigerator.view.login.SignupActivity

class ErrandFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_errand, container, false)

        val btnAddErrand : FloatingActionButton = view.findViewById(R.id.btn_add_errand)
        btnAddErrand.setOnClickListener {
            var intent = Intent(view.context, CreateErrandActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


        return view
    }
}