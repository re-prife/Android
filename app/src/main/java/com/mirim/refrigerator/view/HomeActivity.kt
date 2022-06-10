package com.mirim.refrigerator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
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
import java.io.File
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

        binding.imageKing1.clipToOutline = true
        binding.imageKing2.clipToOutline = true
        binding.imageKing3.clipToOutline = true
        binding.imageKing4.clipToOutline = true
        monthOfKings()


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
            val intent = Intent(applicationContext, NoticeActivity::class.java)
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
                val raw = response.raw()
                val body = response.body()

                Log.d(TAG,raw.toString())
                Log.d(TAG,body.toString())

                when(raw.code()) {
                    200 -> {
                        if(body?.questKingResponse != null) {
                            val str = "심부름 "+body.questKingResponse.count+"회"
                            binding.txtKingName1.isVisible = true
                            binding.txtKingHousework1.isVisible = true
                            binding.txtKingNone1.isVisible = false

                            binding.txtKingName1.text = body.questKingResponse.userNickname
                            binding.txtKingHousework1.text = str
                            Glide.with(applicationContext)
                                .load(RetrofitService.IMAGE_BASE_URL+body.questKingResponse.userImagePath)
                                .error(R.drawable.icon_profile)
                                .fallback(R.drawable.icon_profile)
                                .into(binding.imageKing4)
                        }
                        for(item in body?.choreKingResponse!!) {
                            when (item.category) {
                                "DISH_WASHING" -> {
                                    val str = "설거지 "+item.count+"회"

                                    binding.txtKingName1.isVisible = true
                                    binding.txtKingHousework1.isVisible = true
                                    binding.txtKingNone1.isVisible = false

                                    binding.txtKingName1.text = item.userNickname
                                    binding.txtKingHousework1.text = str
                                    Glide.with(applicationContext)
                                        .load(RetrofitService.IMAGE_BASE_URL+item.userImagePath)
                                        .error(R.drawable.icon_profile)
                                        .fallback(R.drawable.icon_profile)
                                        .into(binding.imageKing1)
                                }
                                "COOK" -> {
                                    val str = "요리 "+item.count+"회"

                                    binding.txtKingName2.isVisible = true
                                    binding.txtKingHousework2.isVisible = true
                                    binding.txtKingNone2.isVisible = false

                                    binding.txtKingName2.text = item.userNickname
                                    binding.txtKingHousework2.text = str
                                    Glide.with(applicationContext)
                                        .load(RetrofitService.IMAGE_BASE_URL+item.userImagePath)
                                        .error(R.drawable.icon_profile)
                                        .fallback(R.drawable.icon_profile)
                                        .into(binding.imageKing2)
                                }
                                "SHOPPING" -> {
                                    val str = "장보기 "+item.count+"회"

                                    binding.txtKingName3.isVisible = true
                                    binding.txtKingHousework3.isVisible = true
                                    binding.txtKingNone3.isVisible = false

                                    binding.txtKingName3.text = item.userNickname
                                    binding.txtKingHousework3.text = str
                                    Glide.with(applicationContext)
                                        .load(RetrofitService.IMAGE_BASE_URL+item.userImagePath)
                                        .error(R.drawable.icon_profile)
                                        .fallback(R.drawable.icon_profile)
                                        .into(binding.imageKing3)
                                }
                            }
                        }
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

    private fun getDate() : String {
        val currentTime = System.currentTimeMillis()
        return formatDate(currentTime)
    }
    private fun formatDate(time : Long) : String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREA)
        return sdf.format(time)
    }
}