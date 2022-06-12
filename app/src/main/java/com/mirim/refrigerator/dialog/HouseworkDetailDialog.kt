package com.mirim.refrigerator.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.databinding.DialogHouseworkDetailBinding
import com.mirim.refrigerator.model.Housework
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback

class HouseworkDetailDialog(val housework: Housework?) :DialogFragment() {
    lateinit var binding: DialogHouseworkDetailBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogHouseworkDetailBinding.inflate(requireActivity().layoutInflater)

            binding.txtHouseworkCategory.text = Housework.categoryKoreanConverter(housework?.choreCategory)
            binding.txtHouseworkName.text = housework?.choreTitle
            binding.txtHouseworkAssignee.text = housework?.userId.toString()
            binding.txtHouseworkPerformDate.text = housework?.choreDate
            binding.txtHouseworkRegisterDate.text = housework?.createdDate
            binding.txtHouseworkModifyDate.text = housework?.modifiedDate

            binding.btnApprove.setOnClickListener {
                Toast.makeText(context, "인증받기", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }

            binding.btnDelete.setOnClickListener {
                deleteChore();
                Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }

            // 모서리 둥글게 하는 코드
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

            builder.setView(binding.root)
            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")


    }
    fun deleteChore() {
        RetrofitService.serviceAPI.deleteChore(App.user.groupId, housework?.choreId).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Log.d("HouseworkDetailDialog-deleteChore", response.toString())
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

}