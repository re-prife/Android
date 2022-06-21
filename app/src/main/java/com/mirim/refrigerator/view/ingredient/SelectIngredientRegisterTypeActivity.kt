package com.mirim.refrigerator.view.ingredient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.mirim.refrigerator.databinding.ActivitySelectIngredientRegisterTypeBinding
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SelectIngredientRegisterTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectIngredientRegisterTypeBinding
    private var TAG = "TAG_SELECT_INGREDIENT_TYPE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectIngredientRegisterTypeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        binding.btnRegisterIngredient.setOnClickListener {
            val intent = Intent(applicationContext,IngredientRegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
        binding.btnScanQr.setOnClickListener {
            startBarcodeReader()
        }

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    fun startBarcodeReader() {
        val integrator = IntentIntegrator(this)
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.captureActivity = QrScanActivity::class.java
        integrator.initiateScan()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(result != null) {
            if(result.contents != null) {

                // QR 제공 정보
                var ingredientName : String? = null
                var ingredientSaveType : String?  = null
                var ingredientExpirationDate : String?  = null
                var ingredientCategory : String?  = null
                // 추가 정보i
                var ingredientPurchaseDate : String = getPurchaseDate()

                try {
                    val obj : JSONObject = JSONObject(result.contents)
                    ingredientName = obj.getString("ingredientName")
                    ingredientSaveType = obj.getString("ingredientSaveType")
                    ingredientExpirationDate = obj.getString("ingredientExpirationDate")
                    ingredientCategory = obj.getString("ingredientCategory")
                } catch (e : Exception) {
                    Log.d(TAG, e.message.toString())
                    Toast.makeText(this,"QR스캔중 예외가 발생하였습니다.",Toast.LENGTH_SHORT).show()
                }

                // 식재료 수기 등록으로 이동
                val intent = Intent(applicationContext,IngredientRegisterActivity::class.java)
                intent.putExtra("ingredientName",ingredientName)
                intent.putExtra("ingredientSaveType",ingredientSaveType)
                intent.putExtra("ingredientExpirationDate",ingredientExpirationDate)
                intent.putExtra("ingredientCategory",ingredientCategory)
                intent.putExtra("ingredientPurchaseDate",ingredientPurchaseDate)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()


            } else {
                Toast.makeText(this,"QR스캔이 취소되었습니다.",Toast.LENGTH_SHORT).show()
                Log.d(TAG,"Cancelled")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            Log.d(TAG,"RESULT IS NULL")
            Toast.makeText(this,"유효한 QR코드를 스캔해주세요.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPurchaseDate(): String {
        val currentTime = System.currentTimeMillis()
        return formatDate(currentTime)
    }
    private fun formatDate(time : Long) : String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        return sdf.format(time)
    }
}