package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirim.refrigerator.adapter.ErrandListAdapter
import com.mirim.refrigerator.databinding.FragmentErrandBinding
import com.mirim.refrigerator.dialog.ErrandAllowedDialog
import com.mirim.refrigerator.dialog.ErrandArrivedDialog
import com.mirim.refrigerator.model.Errand
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.view.errand.CreateErrandActivity
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ErrandFragment: Fragment() {

    var _binding : FragmentErrandBinding? = null
    private val binding get() = _binding!!
    private val userViewModel : UserViewModel by viewModels()
    lateinit var errandListAdapter : ErrandListAdapter

    companion object {
        val TAG = "태그"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentErrandBinding.inflate(inflater,container,false)
        val view = binding.root


        setErrandList()


        binding.btnAddErrand.setOnClickListener {
            var intent = Intent(view.context, CreateErrandActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.btnArrivedDialog.setOnClickListener {
            val dialog = ErrandArrivedDialog("심부름 제목", listOf("엄마","아빠"))
            dialog.show(requireActivity().supportFragmentManager,"")
        }
        binding.btnAllowedDialog.setOnClickListener {
            val dialog = ErrandAllowedDialog("심부름 제목", "아빠")
            dialog.show(requireActivity().supportFragmentManager,"")
        }

        return view
    }

    private fun setErrandList() {
        userViewModel.loadUsers(App.user)
        userViewModel.setFamilyList(App.family)
        RetrofitService.errandAPI.getErrandList(userViewModel.getGroupId()!!).enqueue(object : Callback<List<Errand>> {
            override fun onResponse(call: Call<List<Errand>>, response: Response<List<Errand>>) {
                val body = response.body()
                val raw = response.raw()
                Log.e(TAG,body.toString())
                when(raw.code) {
                    200 -> {
                        if(body != null) {
                            errandListAdapter = ErrandListAdapter(
                                activity,
                                errandList = body,
                            )
                            binding.listRecycle.adapter = errandListAdapter
                            binding.listRecycle.layoutManager = LinearLayoutManager(context)
                        }

                    }
                    404 -> {
                        Toast.makeText(activity,"심부름 조회 중 오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Errand>>, t: Throwable) {
                Toast.makeText(activity,t.message,Toast.LENGTH_SHORT).show()
                Log.e(TAG,t.message.toString())
            }

        })
    }
}