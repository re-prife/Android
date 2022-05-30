package com.mirim.refrigerator.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.databinding.DialogErrandArrivedBinding
import java.lang.IllegalStateException

class ErrandAllowedDialog(var errandTitle : String, var accepter : String) : DialogFragment() {

    lateinit var binding: DialogErrandArrivedBinding

    private val accepterAdditionText = "By. "


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogErrandArrivedBinding.inflate(requireActivity().layoutInflater)

            binding.txtTitle.text = errandTitle
            binding.txtAccepter.text = accepterAdditionText+accepter
            binding.btnAccept.setOnClickListener {
                Toast.makeText(context, "수락됨", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }
            binding.btnIgnore.setOnClickListener {
                Toast.makeText(context, "무시됨", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }

            builder.setView(binding.root)
            builder.create()
        }?: throw IllegalStateException("Activiy cannot be null")
    }
}