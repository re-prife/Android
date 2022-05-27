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

class IngredientModifyDialog(var ingredientNames: List<String>, var ingredientAmounts: List<String>) : DialogFragment() {
    lateinit var binding:DialogIngredientModifyBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogIngredientModifyBinding.inflate(requireActivity().layoutInflater)

            var index = 0
            var length = ingredientNames.size

//            binding.txtIngredientName.text = ingredientNames[index]
//            binding.editModifyAmount.setText(ingredientAmounts[index])

            binding.btnPrev.setOnClickListener {
                if(index > 0) {
                    index++
                    binding.txtIngredientName.text = ingredientNames[index]
                    binding.editModifyAmount.setText(ingredientAmounts[index])
                }
            }

            binding.btnNext.setOnClickListener {
                if(index < length-1) {
                    index--
                    binding.txtIngredientName.text = ingredientNames[index]
                    binding.editModifyAmount.setText(ingredientAmounts[index])
                }
            }

            binding.btnConfirm.setOnClickListener {
                Toast.makeText(context, "확인", Toast.LENGTH_SHORT).show()
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