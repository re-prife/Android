package com.mirim.refrigerator.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.mirim.refrigerator.databinding.DialogHouseworkDetailBinding
import com.mirim.refrigerator.model.Housework
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.network.SocketHandler
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.server.socket.CertifyChore
import com.mirim.refrigerator.server.socket.CertifyChoreData
import com.mirim.refrigerator.viewmodel.App
import io.socket.client.Socket
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class HouseworkDetailDialog(val housework: Housework?, val mContext: Context?) :DialogFragment() {
    lateinit var binding: DialogHouseworkDetailBinding
    lateinit var socket: Socket

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(context)
            binding = DialogHouseworkDetailBinding.inflate(requireActivity().layoutInflater)

            binding.txtHouseworkCategory.text = Housework.categoryKoreanConverter(housework?.choreCategory)
            binding.txtHouseworkName.text = housework?.choreTitle
            binding.txtHouseworkAssignee.text = App.getFamilyMember(housework?.userId)?.userNickname
            binding.txtHouseworkPerformDate.text = housework?.choreDate
            binding.txtHouseworkRegisterDate.text = housework?.createdDate
            binding.txtHouseworkModifyDate.text = housework?.modifiedDate

            if(housework?.choreCheck == "BEFORE") {
                binding.linearHouseworkConfirm.visibility = View.VISIBLE
                binding.txtHouseworkStatus.visibility = View.GONE
            }
            else if(housework?.choreCheck == "REQUEST") {
                binding.linearHouseworkConfirm.visibility = View.GONE
                binding.txtHouseworkStatus.visibility = View.VISIBLE
                binding.txtHouseworkStatus.text = "인증대기중"
            }
            else if(housework?.choreCheck == "SUCCESS") {
                binding.linearHouseworkConfirm.visibility = View.GONE
                binding.txtHouseworkStatus.visibility = View.VISIBLE
                binding.txtHouseworkStatus.text = "완료된 집안일입니다."
            }
            else if(housework?.choreCheck == "FAIL") {
                binding.linearHouseworkConfirm.visibility = View.GONE
                binding.txtHouseworkStatus.visibility = View.VISIBLE
                binding.txtHouseworkStatus.text = "실패한 집안일입니다."
            }

            binding.btnApprove.setOnClickListener {
                if(housework?.userId == App.user.userId) {
                    confirmHousework()
                }
                else {
                    Toast.makeText(context, "내가 한 집안일이 아니에요 ㅠㅠ", Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }
            }

            binding.btnDelete.setOnClickListener {
                deleteChore();
            }

            // 모서리 둥글게 하는 코드
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

            builder.setView(binding.root)
            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")


    }
    fun deleteChore() {
        RetrofitService.houseworkAPI.deleteChore(App.user.groupId, housework?.choreId).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Log.d("HouseworkDetailDialog-deleteChore", response.toString())
                Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("HouseworkDetailDialog-deleteChore", t.toString())
            }

        })

    }

    fun confirmHousework() {
        Log.d("HouseworkDetailDialog", "confirmHousework")
        socket = SocketHandler.getterSocket()
        var data = CertifyChore(
            category = housework?.choreCategory,
            userNickname = App.getFamilyMember(housework?.userId)?.userNickname,
            title = housework?.choreTitle
        )
        socket.emit("certifyChore", JSONObject(Gson().toJson(CertifyChoreData(data))))
        dialog?.dismiss()
    }

}