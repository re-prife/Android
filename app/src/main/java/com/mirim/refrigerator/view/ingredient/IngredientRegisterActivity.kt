package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientRegisterBinding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.CreateIngredientRequest
import com.mirim.refrigerator.server.responses.CreateGroupResponse
import com.mirim.refrigerator.server.responses.CreateIngredientResponse
import com.mirim.refrigerator.view.BottomAppBarActivity
import com.mirim.refrigerator.viewmodel.App
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat

class IngredientRegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityIngredientRegisterBinding

    private val REQUEST_GET_IMAGE = 999
    private lateinit var uploadFile : MultipartBody.Part
    var isImageChanged = false

    val TAG = "IngredientRegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientRegisterBinding.inflate(layoutInflater)
        val view = binding.root


        // QR스캔 확인
        if(intent.getStringExtra("ingredientName") != null) {
            val intent_saveType = intent.getStringExtra("ingredientSaveType")
            val intent_category = intent.getStringExtra("ingredientCategory")

            binding.editName.setText(intent.getStringExtra("ingredientName"))
            binding.editEndDay.setText(intent.getStringExtra("ingredientExpirationDate"))
            binding.editBoughtDay.setText(intent.getStringExtra("ingredientPurchaseDate"))
        }

        val categoryAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.ingredient_category, android.R.layout.simple_spinner_item)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        val saveTypeAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.ingredient_saveType, android.R.layout.simple_spinner_item)
        saveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKeepType.adapter = saveTypeAdapter

        binding.iconCamera.setOnClickListener {
            openGallery()
        }

        binding.toolbar.btnBack.setOnClickListener {
           finish()
        }
        binding.toolbar.toolbarTitle.text = "식재료 등록"
        binding.btnSaveIngredient.setOnClickListener {
            val ingredientName = binding.editName.text.toString()
            val ingredientAmount = binding.editAmount.text.toString()
            val ingredientPurchaseDate = binding.editBoughtDay.text.toString()
            val ingredientExpirationDate = binding.editEndDay.text.toString()
            val ingredientCategory = Ingredient.typeEnglishConverter(binding.spinnerCategory.selectedItem.toString())
            val ingredientKeepType = Ingredient.storeEnglishConverter(binding.spinnerKeepType.selectedItem.toString())
            val ingredientMemo = binding.editMemo.text.toString()

            if(!ingredientAmount[0].isDigit()) {
                Toast.makeText(applicationContext, "식재료 수량은 숫자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                val dateFormat = SimpleDateFormat("yyyyMMdd")
                val dateFormat2 = SimpleDateFormat("yyyy-MM-dd")

                val objDate1 = dateFormat.parse(binding.editBoughtDay.text.toString())
                val finalDate = dateFormat2.format(objDate1)

                val objDate2 = dateFormat.parse(binding.editEndDay.text.toString())
                val finalDate2 = dateFormat2.format(objDate2)
                createIngredient(CreateIngredientRequest(ingredientName = ingredientName, ingredientCount = ingredientAmount, ingredientPurchaseDate = finalDate, ingredientExpirationDate = finalDate2,
                    ingredientCategory = ingredientCategory, ingredientSaveType = ingredientKeepType, ingredientMemo = ingredientMemo))
            }

        }
        binding.btnCancelIngredient.setOnClickListener {
            finish()
        }

        setContentView(view)
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_GET_IMAGE -> {
                    val inputStream: InputStream?
                    val uri = data?.data

                    try {
                        inputStream = applicationContext.contentResolver.openInputStream(uri!!)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                        val requestBody = RequestBody.create(
                            MediaType.parse("image/jpeg"),
                            byteArrayOutputStream.toByteArray()
                        )
                        uploadFile = MultipartBody.Part.createFormData(
                            "file",
                            "upload_ingredient.jpg",
                            requestBody
                        )
                        isImageChanged = true
                        binding.imageIngredient.setImageURI(uri)
                    } catch (e: Exception) {
                        Log.e(TAG, e.message.toString())
                        Toast.makeText(baseContext, "갤러리를 여는 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        } else {
            Log.d(TAG, "사진 선택 취소")
            Toast.makeText(applicationContext, "이미지 선택 취소", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createIngredient(data: CreateIngredientRequest) {
        RetrofitService.serviceAPI.createIngredients(App.user.groupId, data).enqueue(object : Callback<CreateIngredientResponse> {
            override fun onResponse(
                call: Call<CreateIngredientResponse>,
                response: Response<CreateIngredientResponse>
            ) {
                if(response.raw().code() == 201) {
                    if(isImageChanged) uploadIngredientImage(response.body()?.ingredientId)
                    Toast.makeText(applicationContext, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Toast.makeText(applicationContext, "저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
                Log.d("IngredientRegisterActivity-createIngredient", response.toString())

            }

            override fun onFailure(call: Call<CreateIngredientResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "실패하였습니다.", Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun uploadIngredientImage(ingredientId : Long?) {
        RetrofitService.ingredientAPI.uploadIngredientImage(ingredientId, uploadFile).enqueue(object : Callback<com.mirim.refrigerator.server.responses.Response> {
            override fun onResponse(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                response: Response<com.mirim.refrigerator.server.responses.Response>
            ) {
                Log.d(TAG, response.toString())
                Log.d(TAG, response.raw().message())
            }

            override fun onFailure(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                t: Throwable
            ) {
                Log.d(TAG, t.toString())
            }

        })
    }
}


