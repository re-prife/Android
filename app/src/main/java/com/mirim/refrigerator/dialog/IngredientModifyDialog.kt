package com.mirim.refrigerator.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.databinding.DialogIngredientModifyBinding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.UpdateMultiIngredientRequest
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback

class IngredientModifyDialog(var ingredients: List<Ingredient>, val mContext: Context?) : DialogFragment() {
    lateinit var binding:DialogIngredientModifyBinding
    var modifiedIngredient = HashMap<Long, String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogIngredientModifyBinding.inflate(requireActivity().layoutInflater)


            var index = 0
            var length = ingredients.size

            for(item in ingredients) {
                modifiedIngredient.set(item.ingredientId, item.ingredientCount!!)
            }

            binding.txtIngredientName.text = ingredients[index].ingredientName
            binding.editModifyAmount.setText(ingredients[index].ingredientCount)

            binding.btnPrev.setOnClickListener {
                if(index > 0) {
                    index--
                }
                binding.txtIngredientName.text = ingredients[index].ingredientName
                binding.editModifyAmount.setText(ingredients[index].ingredientCount)
            }

            binding.btnNext.setOnClickListener {
                if(index < length-1) {
                    index++
                }
                binding.txtIngredientName.text = ingredients[index].ingredientName
                binding.editModifyAmount.setText(ingredients[index].ingredientCount)
            }

            binding.btnConfirm.setOnClickListener {
                updateMultiIngredients(context)
                dialog?.dismiss()
            }

            binding.editModifyAmount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    modifiedIngredient.set(ingredients[index].ingredientId, p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            // 모서리 둥글게 하는 코드
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

            builder.setView(binding.root)
            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")


    }

    fun updateMultiIngredients(curContext: Context?) {
        val list = ArrayList<UpdateMultiIngredientRequest>()
        for(item in modifiedIngredient) {
            list.add(UpdateMultiIngredientRequest(ingredientId = item.key, ingredientCount = item.value))
        }
        RetrofitService.ingredientAPI.updateMultiIngredients(App.user.groupId, list).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if(response.raw().code() == 204) {
                    Toast.makeText(curContext, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                    (curContext as Activity).finish()
                }
                else {
                    Toast.makeText(curContext, "수정에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(curContext, "실패하였습니다.", Toast.LENGTH_SHORT).show()
            }

        })
    }
}