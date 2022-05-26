package com.mirim.refrigerator.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.DialogIngredientDeleteBinding
import com.mirim.refrigerator.databinding.DialogShowCodeBinding

class IngredientDeleteDialog(var ingredientName: String) : DialogFragment() {

    lateinit var binding: DialogIngredientDeleteBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogIngredientDeleteBinding.inflate(requireActivity().layoutInflater)

            binding.txtIngredientName.text = ingredientName
            binding.btnConfirm.setOnClickListener {
                Toast.makeText(context, "확인", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }

            binding.btnCancel.setOnClickListener {
                Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }

            // 모서리 둥글게 하는 코드
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

            builder.setView(binding.root)
            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")


    }

}