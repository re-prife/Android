package com.mirim.refrigerator.view.housework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityRegisterHouseworkBinding
import com.mirim.refrigerator.model.Housework
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.CreateHouseworkRequest
import com.mirim.refrigerator.server.responses.Response
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat

class RegisterHouseworkActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterHouseworkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterHouseworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.housework_category, android.R.layout.simple_spinner_item)

        val memberNameList = ArrayList<String>()
        memberNameList.add(App.user.nickname!!)
        for(member in App.family) {
            memberNameList.add(member.userNickname)
        }
        val nameAdapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item, memberNameList)

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerHouseworkCategory.adapter = categoryAdapter
        binding.spinnerHouseworkAssignee.adapter = nameAdapter




       // binding.editHouseworkPerformDate.addTextChangedListener(DateFormatWatcher(binding.editHouseworkPerformDate))

        binding.btnCreate.setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyyMMdd")
            val objDate = dateFormat.parse(binding.editHouseworkPerformDate.text.toString())
            val dateFormat2 = SimpleDateFormat("yyyy-MM-dd")
            val finalDate = dateFormat2.format(objDate)
            Log.d("RegisterHouseworkActivity", App.getFamilyId(binding.spinnerHouseworkAssignee.selectedItem.toString()).toString())
            val familyId = App.getFamilyId(binding.spinnerHouseworkAssignee.selectedItem.toString())
            createChore(CreateHouseworkRequest(
                choreTitle = binding.editHouseworkName.text.toString(),
                choreCategory = Housework.categoryEnglishConverter(binding.spinnerHouseworkCategory.selectedItem.toString()),
                choreDate =  finalDate,
                choreUserId = if (familyId == null) App.user.userId else familyId
            )
            )
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.toolbar.toolbarTitle.text = "집안일 정하기"
    }
    fun createChore(body: CreateHouseworkRequest) {
        RetrofitService.houseworkAPI.createChore(App.user.groupId, body).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if(response.raw().code() == 201) {
                    Toast.makeText(applicationContext, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Toast.makeText(applicationContext, "집안일 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
                Log.d("RegisterHouseworkActivity", response.toString())

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("RegisterHouseworkActivity", t.toString())
            }

        })
    }
}