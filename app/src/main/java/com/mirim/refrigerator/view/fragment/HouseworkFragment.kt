package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirim.refrigerator.adapter.HouseworkAdapter
import com.mirim.refrigerator.databinding.FragmentHouseworkBinding
import com.mirim.refrigerator.dialog.HouseworkDetailDialog
import com.mirim.refrigerator.model.Housework
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.HouseworkResponse
import com.mirim.refrigerator.view.housework.RegisterHouseworkActivity
import com.mirim.refrigerator.viewmodel.app
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HouseworkFragment: Fragment() {
    var _binding: FragmentHouseworkBinding? = null
    private val binding get() = _binding!!

    var chores: List<Housework>? = mutableListOf()
    var todayDate: String = ""
    var todayMonth: String = ""

    companion object {
        val TAG = "태그"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseworkBinding.inflate(inflater, container, false)
        val view = binding.root

        val current = LocalDate.now()
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        todayDate = current.format(formatter)
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM")
        todayMonth = current.format(formatter2)

        binding.txtTodayDate.text = todayDate

        Log.d("HouseWorkFragment-Date", todayDate+" " + todayMonth)
        getChores();

        binding.btnAddHousework.setOnClickListener {
            var intent = Intent(context, RegisterHouseworkActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        return view
    }

    fun getChores() {
        RetrofitService.serviceAPI.getChores(app.user.groupId, todayMonth).enqueue(object : Callback<HouseworkResponse> {
            override fun onResponse(
                call: Call<HouseworkResponse>,
                response: Response<HouseworkResponse>
            ) {
                if(response.isSuccessful && response.body() != null) {
                    chores = response.body()?.data
                    binding.recyclerHousework.layoutManager = LinearLayoutManager(context)
                    binding.recyclerHousework.setHasFixedSize(false)
                    binding.recyclerHousework.adapter = HouseworkAdapter(context, chores)

                }
            }

            override fun onFailure(call: Call<HouseworkResponse>, t: Throwable) {
            }

        })
    }
}