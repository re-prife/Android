package com.mirim.refrigerator.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.databinding.DialogHouseworkDetailBinding
import com.mirim.refrigerator.databinding.DialogIngredientDeleteBinding

class HouseworkDetailDialog(var category: String, var name: String, var assignee: String, var performDate: String, var registerDate: String, var modifyDate: String) :DialogFragment() {
    lateinit var binding: DialogHouseworkDetailBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogHouseworkDetailBinding.inflate(requireActivity().layoutInflater)

            binding.txtHouseworkCategory.text = category
            binding.txtHouseworkName.text = name
            binding.txtHouseworkAssignee.text = assignee
            binding.txtHouseworkPerformDate.text = performDate
            binding.txtHouseworkRegisterDate.text = registerDate
            binding.txtHouseworkModifyDate.text = modifyDate

            binding.btnApprove.setOnClickListener {
                Toast.makeText(context, "인증받기", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }

            binding.btnDelete.setOnClickListener {
                Toast.makeText(context, "삭제", Toast.LENGTH_SHORT).show()
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