package com.mirim.refrigerator.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityPermissionCheckDialogBinding

class PermissionCheckDialog(context : Context) {

    lateinit var binding : ActivityPermissionCheckDialogBinding
    private val dialog = Dialog(context)

    fun showDialog()
    {
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        binding.btnAccept.setOnClickListener {
            dialog.dismiss()
        }
    }
}