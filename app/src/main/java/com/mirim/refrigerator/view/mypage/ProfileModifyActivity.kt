package com.mirim.refrigerator.view.mypage

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.CursorAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityProfileModifyBinding
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.ModifyUserImageRequest
import com.mirim.refrigerator.server.request.ModifyUserInfoRequest
import com.mirim.refrigerator.view.fragment.MyPageFragment
import com.mirim.refrigerator.viewmodel.App
import com.mirim.refrigerator.viewmodel.UserViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Exception
import java.net.URI

class ProfileModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileModifyBinding
    var checkImage : Boolean = false
    var checkInfo : Boolean = false
    private val REQUEST_GET_IMAGE = 999
    val TAG = "ProfileModeifyActivity"
    var isImageChanged : Boolean = false
    var destFile : File? = null
    private val userViewModel : UserViewModel by viewModels()

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

    private fun checkPermission() {
        val galleryPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if(galleryPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }
    }




    private fun initBaseUserInfo() {
        Glide.with(applicationContext)
            .load(RetrofitService.IMAGE_BASE_URL+App.user.userImagePath)
            .error(R.drawable.icon_profile)
            .fallback(R.drawable.icon_profile)
            .into(binding.userImage)
        binding.editName.setText(App.user.name)
        binding.editNickname.setText(App.user.nickname)
        binding.editEmail.setText(App.user.email)
    }

    private fun openGallery() {
        checkPermission()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK) {
            when(requestCode) {
                REQUEST_GET_IMAGE -> {

                    try {

                        // 상대 경로
                        val uri : Uri? = data?.data
                        // 절대 경로
                        var imagePath : String? = null
                        if(uri != null) {
                            imagePath = getRealPathFromURI(uri)
                            destFile = File(imagePath)
                            isImageChanged = true
                        } else {
                            return
                        }


                    } catch (e : Exception) {
                        Log.e(TAG,e.message.toString())
                        Toast.makeText(baseContext,"갤러리를 여는 도중 오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Log.d(TAG,"사진 선택 취소")
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
                when(raw.code()) {
                    204 -> {
                        checkInfo = true
                        Log.d(TAG,"INFO 성공")
                        if(isImageChanged) {
                            saveImageInfo(destFile!!)
                        }
                        Toast.makeText(applicationContext,"프로필이 성공적으로 변경되었습니다.",Toast.LENGTH_SHORT).show()
                        val new_user = User(data.userNickname,data.userName,App.user.email,App.user.userId,App.user.groupId,App.user.userImagePath)
                        userViewModel.loadUsers(new_user)
                        App.user = new_user
                        Log.e(TAG,App.toString())
                        finish()
                        overridePendingTransition(R.anim.translate_none,R.anim.translate_none)

                    }
                    400 -> {
                        checkInfo = false
                        Log.d(TAG,"Requset형식이 유효하지 않음")
                    }
                    404 -> {
                        checkInfo = false
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


    private fun getRealPathFromURI(uri : Uri?) : String? {
        if (uri != null) {
            if(uri.path!!.startsWith("/storage")) {
                return uri.path!!
            }
        }
        var id : String = DocumentsContract.getDocumentId(uri).split(":")[1]
        var columns : Array<String> = arrayOf(MediaStore.Files.FileColumns.DATA)
        var selection : String = MediaStore.Files.FileColumns._ID+"="+id
        var cursor : Cursor? = application.contentResolver.query(MediaStore.Files.getContentUri("external"),columns,selection,null,null)
        try {
            var columnIndex : Int = cursor?.getColumnIndex(columns[0])!!.toInt()
            if(cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun saveImageInfo(destFile : File) {
        // 이미지 RequestBody로 변환 -> MultipartBody.Part로 컨버전
        val requestBmp = RequestBody.create(MediaType.parse("multipart/form-data"),destFile)
        val bmp = MultipartBody.Part.createFormData("IMG_FILE",destFile.name,requestBmp)

        RetrofitService.userAPI.modifyUserImage(App.user.userId!!,bmp).enqueue(object : Callback<com.mirim.refrigerator.server.responses.Response> {
            override fun onResponse(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                response: Response<com.mirim.refrigerator.server.responses.Response>
            ) {
                val raw = response.raw()
                Log.d(TAG,raw.code().toString())
                when(raw.code()) {
                    200 -> {
                        checkImage = true
                        Log.d(TAG,"이미지 업데이트 성공")
                    }
                    404 -> {
                        checkImage = false
                        Log.d(TAG,"userId가 존재하지 않음")
                    }
                    409 -> {
                        checkImage = false
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

    private fun convertImageToPNG(uri : Uri) : MultipartBody.Part {

        var file = File(uri.toString())
        var fileName : String = App.user.userId.toString() + ".png"


        var requestBody = RequestBody.create(MediaType.parse("image/*"),file)
        var imageFile = MultipartBody.Part.createFormData("up_file",fileName,requestBody)

        return imageFile
//        try {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver,uri))
//            } else {
//                bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
//            }
//        } catch (e : Exception) {
//            Log.e(TAG,"BITMAP 처리중 예외 발생")
//        }
//
//        if(bitmap != null) {
//            try {
//
//                file?.createNewFile()
//                outputStream = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                outputStream.close()
//                Log.d("AAAAAAAAAAAAA",bitmap.toString())
//                return bitmap
//
//            } catch (e : Exception) {
//                Log.i(TAG, e.message.toString())
//            }
//        }
//        return null
    }
}