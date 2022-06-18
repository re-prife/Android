package com.mirim.refrigerator.view

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.model.Notice
import com.mirim.refrigerator.dialog.PermissionCheckDialog
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.HomeKingsResponse
import com.mirim.refrigerator.server.sse.getEventsFlow
import com.mirim.refrigerator.view.fragment.MyPageFragment
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userViewModel : UserViewModel by viewModels()

    companion object {
        var noticeTitle: String = ""
        var noticeContent: String = ""
        val TAG : String = "Home 태그"
    }

    private fun checkPermission() {
        val galleryPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val cameraPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        val internetPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET)
        val snsPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)

        if(galleryPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }
        if(cameraPermission == PackageManager.PERMISSION_DENIED ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),1)
        }
        if(internetPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET),2)
        }
        if(snsPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),3)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userViewModel.loadUsers(App.user)


        setFamilyData()



        // 권한 dialog
        val dialog = PermissionCheckDialog()
        dialog.show(supportFragmentManager,"")
        checkPermission()




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
        getNotice()




        Log.d("AAAAAAAAAAA",App.user.userId.toString())
        // 지켜본다.
        GlobalScope.launch(Dispatchers.Main) {
            getEventsFlow()
                .flowOn(Dispatchers.IO)
                .collect {
                    event -> {
                        Log.e(TAG,event.toString())
                    }
                }
        }

    }

    override fun onRestart() {
        super.onRestart()
        getNotice()
    }


    private fun setFamilyData() {
        RetrofitService.familyAPI.getFamilyList(userViewModel.getGroupId(),userViewModel.getUserId()).enqueue(object : Callback<List<FamilyMember>> {
            override fun onResponse(
                call: Call<List<FamilyMember>>,
                response: Response<List<FamilyMember>>
            ) {
                val body = response.body()
                App.family = body!!
                userViewModel.setFamilyList(response.body()!!)
                // 가족 리스트
                for(i in 0 until userViewModel.getFamily().size)
                    Log.d(MyPageFragment.TAG,userViewModel.getFamily().get(i).userName+" : "+userViewModel.getFamily().get(i).userNickname)
            }
            override fun onFailure(call: Call<List<FamilyMember>>, t: Throwable) {
                Log.e(TAG,"가족 정보 조회 실패")
            }
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

                when(body.) {
                    200 -> {
                        if(body?.questKingResponse != null) {
                            val str = "심부름 "+body.questKingResponse.count+"회"
                            binding.txtKingName4.isVisible = true
                            binding.txtKingHousework4.isVisible = true
                            binding.txtKingNone4.isVisible = false

                            binding.txtKingName4.text = body.questKingResponse.userNickname
                            binding.txtKingHousework4.text = str
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
                        Log.d(TAG,raw.message)
                    }
                    404 -> {
                        Log.d(TAG,raw.message)
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

    fun getNotice() {
        RetrofitService.familyAPI.getNotice(App.user.groupId).enqueue(object : Callback<Notice> {
            override fun onResponse(call: Call<Notice>, response: Response<Notice>) {
                Log.d(TAG, response.toString())
                binding.mainNoticeTitle.text = response.body()?.groupReport
            }

            override fun onFailure(call: Call<Notice>, t: Throwable) {
                Log.d(TAG, t.toString())
            }

        })
    }
}