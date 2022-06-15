package com.mirim.refrigerator.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.DeleteIngredientsRequest
import com.mirim.refrigerator.server.responses.CreateIngredientResponse
import com.mirim.refrigerator.server.responses.DeleteIngredientsResponse
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientDeleteDialog(var ingredientName: String?, var data: List<DeleteIngredientsRequest>, val mContext: Context) : DialogFragment() {

    lateinit var binding: DialogIngredientDeleteBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogIngredientDeleteBinding.inflate(requireActivity().layoutInflater)

            binding.txtIngredientName.text = ingredientName
            binding.btnConfirm.setOnClickListener {
                deleteIngredient(data, mContext)
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

    fun deleteIngredient(data: List<DeleteIngredientsRequest>, curContext: Context) {
        RetrofitService.ingredientAPI.deleteIngredients(App.user.groupId, data).enqueue(object :
            Callback<DeleteIngredientsResponse> {
            override fun onResponse(
                call: Call<DeleteIngredientsResponse>,
                response: Response<DeleteIngredientsResponse>
            ) {
                Log.d("IngredientModifyActivity-deleteIngredient", response.toString())
                (curContext as Activity).finish()
            }

            override fun onFailure(call: Call<DeleteIngredientsResponse>, t: Throwable) {
                Log.d("IngredientDetailActivity-deleteIngredient", t.toString())
            }

        })

    }
}