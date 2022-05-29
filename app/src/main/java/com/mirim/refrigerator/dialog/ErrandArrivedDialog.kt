package com.mirim.refrigerator.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.databinding.DialogIngredientModifyBinding
import com.mirim.refrigerator.databinding.DialogErrandArrivedBinding


class ErrandArrivedDialog(var errandTitle : String, var accepter : List<String>) : DialogFragment() {

    lateinit var binding: DialogErrandArrivedBinding

    // TODO : Dialog 구현
    /*
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogErrandArrivedBinding.inflate(requireActivity().layoutInflater)



            builder.create()

        }? : throw IllegalStateException("Activity cannot be null")
    }

     */
}