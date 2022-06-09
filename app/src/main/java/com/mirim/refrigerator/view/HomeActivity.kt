package com.mirim.refrigerator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.model.ChoreKing
import com.mirim.refrigerator.model.Notice
import com.mirim.refrigerator.model.QuestKing
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.HomeKingsResponse
import com.mirim.refrigerator.viewmodel.NoticeViewModel
import com.mirim.refrigerator.viewmodel.UserViewModel
import com.mirim.refrigerator.viewmodel.app
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val noticeViewModel : NoticeViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()

    companion object {
        var noticeTitle: String = ""
        var noticeContent: String = ""
        val TAG : String = "Home 태그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userViewModel.loadUsers(User(app.user.nickname,app.user.name,app.user.email,app.user.userId,app.user.groupId, app.user.userImagePath))

        // 각 달의 왕들을 받아옴 (userId포함)
        // onResponse의 200일 경우 userId에 대하여 회원 상세 조회, 프로필 사진과 별명 불러옴
        //monthOfKings()


        val intent = Intent(applicationContext, BottomAppBarActivity::class.java)

        binding.buttonRefrigerator.setOnClickListener {
            intent.putExtra("clicked button", "refrigerator")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        binding.buttonHousework.setOnClickListener {
            intent.putExtra("clicked button", "housework")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.buttonErrand.setOnClickListener {
            intent.putExtra("clicked button", "errand")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        binding.buttonMakeErrand.setOnClickListener {
            intent.putExtra("clicked button", "make errand")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra("route",0)
            startActivity(intent)
        }
        binding.buttonMyPage.setOnClickListener {
            intent.putExtra("clicked button", "my page")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        binding.writeNotice.setOnClickListener {
            var intent = Intent(applicationContext, NoticeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        noticeViewModel.getNotice().observe(this, Observer<Notice> {
            binding.mainNoticeTitle.text = it.title
            binding.mainNoticeContent.text = it.contents
        })

    }
    private fun monthOfKings() {
        val groupId : Int? = userViewModel.getGroupId()
        val dateValue : String = getDate()
        RetrofitService.kingAPI.getMonthKing(groupId!!, dateValue).enqueue(object : Callback<HomeKingsResponse> {
            override fun onResponse(
                call: Call<HomeKingsResponse>,
                response: Response<HomeKingsResponse>
            ) {
                var raw = response.raw()
                var body = response.body()

                Log.d(TAG,raw.toString())
                Log.d(TAG,body?.choreKing.toString())
                Log.d(TAG,body?.choreKing?.size.toString())
                Log.d(TAG,body?.questKing.toString())



                when(raw.code()) {
                    200 -> {
                        // userId 추출 및 저장
                        val choreKingsUserIdList = ArrayList<Int>()
                        for (choreKingData in body?.choreKing!!) {
                            choreKingsUserIdList.add(choreKingData.userId)
                        }
                        val questKingUserId = body.questKing.userId

                        // user정보 불러옴 (return : choreKingProfileList - [], questKingProfileMap? - nullable)
                        getUserInfo(choreKingsUserIdList,questKingUserId)





                    }
                    400 -> {
                        Log.d(TAG,raw.message())
                    }
                    404 -> {
                        Log.d(TAG,raw.message())
                    }
                }
            }

            override fun onFailure(call: Call<HomeKingsResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                Log.d(TAG,"fail")
                Toast.makeText(applicationContext,"이달의 왕 정보 조회에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUserInfo(choreKingUserId : ArrayList<Int>?, questKingUserId : Int?)    {



    }



    private data class kingValue(
        var imgSrc : Int?,
        var kingName : String?,
        var category : String,
        var count : String
    )
    private data class BindingValue(
        var img : ImageView,
        var name : TextView,
        var cateCount : TextView
    )
    private fun setKingsInfo(body: HomeKingsResponse?) {

        val idValue = arrayOf(
            BindingValue(binding.imageKing1,binding.txtKingName1,binding.txtKingHousework1),
            BindingValue(binding.imageKing2,binding.txtKingName2,binding.txtKingHousework2),
            BindingValue(binding.imageKing3,binding.txtKingName3,binding.txtKingHousework3),
            BindingValue(binding.imageKing4,binding.txtKingName4,binding.txtKingHousework4),
        )

        val length : Int? = body?.choreKing?.size

    }
    private fun getDate() : String {
        val currentTime = System.currentTimeMillis()
        return formatDate(currentTime)
    }
    private fun formatDate(time : Long) : String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREA)
        return sdf.format(time)
    }
}