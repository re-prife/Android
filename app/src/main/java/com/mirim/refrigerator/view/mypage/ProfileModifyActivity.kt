package com.mirim.refrigerator.view.mypage

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityProfileModifyBinding
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.ModifyUserInfoRequest
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.App.imageUri
import com.mirim.refrigerator.viewmodel.UserViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Exception

class ProfileModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileModifyBinding
    private val userViewModel : UserViewModel by viewModels()
    private val REQUEST_GET_IMAGE = 999
    var isImageChanged : Boolean = false
    val TAG = "ProfileModeifyActivity"

    private lateinit var uploadFile : MultipartBody.Part
    private var uri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBaseUserInfo()


        // 이메일 수정 불가
        binding.editEmail.setOnClickListener {
            Toast.makeText(applicationContext,"이메일은 수정하실 수 없습니다.",Toast.LENGTH_SHORT).show()
        }

        // 비밀번호 수정
        binding.btnModifyPassword.setOnClickListener {
            val intent = Intent(applicationContext, PasswordModifyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        // 저장
        binding.btnComplete.setOnClickListener {
            checkSaveProfile()
        }

        // 프로필 사진 설정
        binding.profileImage.setOnClickListener {
            openGallery()
        }
    }


    // 기존 프로필 내용 적용
    private fun initBaseUserInfo() {
        if(App.imageUri != null) binding.userImage.setImageURI(App.imageUri)
        else {
            Glide.with(applicationContext)
                .load(RetrofitService.IMAGE_BASE_URL+userViewModel.getImage())
                .error(R.drawable.icon_profile)
                .fallback(R.drawable.icon_profile)
                .into(binding.userImage)
        }

        binding.editName.setText(App.user.name)
        binding.editNickname.setText(App.user.nickname)
        binding.editEmail.setText(App.user.email)
    }

    // 갤러리 접근 권한 확인
    private fun checkPermission() {
        val galleryPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if(galleryPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }
    }

    private fun openGallery() {
        checkPermission()
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK) {
            when(requestCode) {
                REQUEST_GET_IMAGE -> {
                    val inputStream : InputStream?
                    uri = data?.data
                    try {
                        inputStream = applicationContext.contentResolver.openInputStream(uri!!)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream)
                        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(),byteArrayOutputStream.toByteArray())
                        uploadFile = MultipartBody.Part.createFormData("file","upload_${App.user.userId}.jpg",requestBody)
                        imageUri = uri as Uri
                        binding.userImage.setImageURI(uri)
                        isImageChanged = true
                    } catch (e : Exception) {
                        Log.e(TAG,e.message.toString())
                        Toast.makeText(baseContext,"갤러리를 여는 도중 오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Log.d(TAG,"사진 선택 취소")
            isImageChanged = false
            Toast.makeText(applicationContext,"이미지 선택 취소",Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkSaveProfile() {
        val nameValue : String = binding.editName.text.toString().trim()
        val nicknameValue : String = binding.editNickname.text.toString().trim()
        var check = true

        // 조건 확인
        when {
            nameValue.isEmpty() -> {
                binding.editName.error = "이름을 입력하세요."
                check = false
            }
            nicknameValue.isEmpty() -> {
                binding.editNickname.error = "닉네임을 입력하세요."
                check = false
            }
        }

        if(check) {
            saveBaseInfo(
                ModifyUserInfoRequest(
                userName = nameValue,
                userNickname = nicknameValue
            ))
        }
    }


    private fun saveBaseInfo(data : ModifyUserInfoRequest) {
        RetrofitService.userAPI.modifyUser(App.user.userId!!,data).enqueue(object : Callback<com.mirim.refrigerator.server.responses.Response> {
            override fun onResponse(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                response: Response<com.mirim.refrigerator.server.responses.Response>
            ) {
                val raw = response.raw()
                when(raw.code) {
                    204 -> {
                        val newUser : User
                        if(isImageChanged) {
                            saveImageInfo()
                            newUser = User(data.userNickname,data.userName,App.user.email,App.user.userId,App.user.groupId,uri.toString())
                        } else {
                            newUser = User(data.userNickname,data.userName,App.user.email,App.user.userId,App.user.groupId,App.user.userImagePath)
                        }
                        Toast.makeText(applicationContext,"프로필이 성공적으로 변경되었습니다.",Toast.LENGTH_SHORT).show()
                        userViewModel.loadUsers(newUser)
                        App.user = newUser
                        finish()
                        overridePendingTransition(R.anim.translate_none,R.anim.translate_none)

                    }
                    400 -> {
                        Log.d(TAG,"Requset형식이 유효하지 않음")
                    }
                    404 -> {
                        Log.d(TAG,"userId가 존재하지 않음")
                    }
                }
            }
            override fun onFailure(call: Call<com.mirim.refrigerator.server.responses.Response>, t: Throwable)  {
                Log.d(TAG,t.message.toString())
                Toast.makeText(applicationContext,"회원 정보 수정에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun saveImageInfo() {
        RetrofitService.userAPI.modifyUserImage(App.user.userId!!,uploadFile).enqueue(object : Callback<com.mirim.refrigerator.server.responses.Response> {
            override fun onResponse(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                response: Response<com.mirim.refrigerator.server.responses.Response>
            ) {
                val raw = response.raw()
                when(raw.code) {
                    200 -> {
                        Log.d(TAG,"이미지 업데이트 성공")
                    }
                    404 -> {
                        Log.d(TAG,"userId가 존재하지 않음")
                    }
                    409 -> {
                        Log.d(TAG,"이미지 파일이 존재하지 않음")
                    }
                }
            }

            override fun onFailure(call: Call<com.mirim.refrigerator.server.responses.Response>, t: Throwable) {
                Log.d(TAG,t.message.toString())
                Toast.makeText(applicationContext,"회원 정보 수정에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        })


    }
}