package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivityIngredientModifyBinding
import com.mirim.refrigerator.model.Ingredient
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.request.CreateIngredientRequest
import com.mirim.refrigerator.server.responses.CreateIngredientResponse
import com.mirim.refrigerator.viewmodel.App
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat

class IngredientModifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityIngredientModifyBinding
    lateinit var ingredient: Ingredient
    private val REQUEST_GET_IMAGE = 999
    var mediaPath: String? = null
    var imageChanged = false

    val TAG = "IngredientModifyActivity"

    private lateinit var uploadFile : MultipartBody.Part
    private var uri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.ingredient_category, android.R.layout.simple_spinner_item)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        val saveTypeAdapter = ArrayAdapter.createFromResource(applicationContext, R.array.ingredient_saveType, android.R.layout.simple_spinner_item)
        saveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKeepType.adapter = saveTypeAdapter

        binding.spinnerCategory

        ingredient = intent.getBundleExtra("bundle")?.getParcelable("ingredient")!!

        binding.editName.setText(ingredient?.ingredientName)
        binding.editAmount.setText(ingredient?.ingredientCount)
        binding.editBoughtDay.setText(ingredient?.ingredientPurchaseDate)
        binding.editEndDay.setText(ingredient?.ingredientExpirationDate)
        binding.spinnerCategory.setSelection(Ingredient.categoryIndex(ingredient?.ingredientCategory))
        binding.spinnerKeepType.setSelection(Ingredient.storeIndex(ingredient?.ingredientSaveType))
        binding.editMemo.setText(ingredient?.ingredientMemo)
        Glide.with(applicationContext).load(RetrofitService.IMAGE_BASE_URL+ingredient?.ingredientImagePath).into(binding.imageIngredient);

        binding.toolbar.toolbarTitle.text = "식재료 수정"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.linearImageIngredient.setOnClickListener {
            openGallery()
        }

        binding.btnSaveIngredient.setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyyMMdd")
            val dateFormat2 = SimpleDateFormat("yyyy-MM-dd")

            val objDate1 = dateFormat.parse(binding.editBoughtDay.text.toString())
            val finalDate = dateFormat2.format(objDate1)

            val objDate2 = dateFormat.parse(binding.editEndDay.text.toString())
            val finalDate2 = dateFormat2.format(objDate2)

            val updatedIngredient = CreateIngredientRequest(
                ingredientCategory = Ingredient.typeEnglishConverter(binding.spinnerCategory.selectedItem.toString()),
                ingredientCount = binding.editAmount.text.toString(),
                ingredientExpirationDate = finalDate2,
                ingredientMemo = binding.editMemo.text.toString(),
                ingredientName = binding.editName.text.toString(),
                ingredientPurchaseDate = finalDate,
                ingredientSaveType = Ingredient.storeEnglishConverter(binding.spinnerKeepType.selectedItem.toString()),
            )
            updateIngredient(updatedIngredient)

        }

        binding.btnCancelIngredient.setOnClickListener {
            Toast.makeText(applicationContext, "취소", Toast.LENGTH_SHORT).show()
        }

    }
    fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK) {
            when(requestCode) {
                REQUEST_GET_IMAGE -> {
                    imageChanged = true
                    val inputStream : InputStream?
                    val uri = data?.data

                    try {
                        inputStream = applicationContext.contentResolver.openInputStream(uri!!)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream)
                        val requestBody = RequestBody.create(MediaType.parse("image/jpeg"),byteArrayOutputStream.toByteArray())
                        uploadFile = MultipartBody.Part.createFormData("file","upload_ingredient_${ingredient.ingredientId}.jpg",requestBody)
                        binding.imageIngredient.setImageURI(uri)
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

    fun updateIngredient(body: CreateIngredientRequest) {
        RetrofitService.serviceAPI.updateIngredients(App.user.groupId, ingredient?.ingredientId, body).enqueue(object : Callback<CreateIngredientResponse> {
            override fun onResponse(
                call: Call<CreateIngredientResponse>,
                response: Response<CreateIngredientResponse>
            ) {
                Log.d("IngredientModifyAcitivity", response.toString())
                if(response.isSuccessful) {
                    if(response.code() == 204) {
                        if(imageChanged) {
                            updateIngredientImage()
                        }
                        else {
                            Toast.makeText(applicationContext, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CreateIngredientResponse>, t: Throwable) {
                Log.d(TAG, t.toString())
            }
        })
    }

    fun updateIngredientImage() {
        RetrofitService.ingredientAPI.uploadIngredientImage(ingredient.ingredientId, uploadFile).enqueue(object : Callback<com.mirim.refrigerator.server.responses.Response> {
            override fun onResponse(
                call: Call<com.mirim.refrigerator.server.responses.Response>,
                response: Response<com.mirim.refrigerator.server.responses.Response>
            ) {
                Log.d(TAG, response.toString())
                Log.d(TAG, response.raw().message())
                Toast.makeText(applicationContext, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
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