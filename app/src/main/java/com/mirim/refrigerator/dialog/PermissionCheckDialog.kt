package com.mirim.refrigerator.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityPermissionCheckDialogBinding

class PermissionCheckDialog() : DialogFragment() {

    lateinit var binding : ActivityPermissionCheckDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = ActivityPermissionCheckDialogBinding.inflate(requireActivity().layoutInflater)

            binding.btnAccept.setOnClickListener {
                dialog?.dismiss()
            }
            builder.setView(binding.root)
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}