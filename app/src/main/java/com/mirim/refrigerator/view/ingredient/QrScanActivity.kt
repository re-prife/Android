package com.mirim.refrigerator.view.ingredient

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import com.mirim.refrigerator.databinding.ActivityQrScanBinding
import com.mirim.refrigerator.dialog.ConfirmBarcodeScanDialog
import com.mirim.refrigerator.dialog.QRErrorDialog
import com.mirim.refrigerator.model.Barcode
import com.mirim.refrigerator.extension.usecase.Settings

class QrScanActivity : AppCompatActivity(), ConfirmBarcodeScanDialog.Listener {

    private lateinit var binding: ActivityQrScanBinding

    private lateinit var codeScanner : CodeScanner
    private var qrScanResult : Barcode? = null

    companion object {
        private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }

    /* 카메라 초기화 설정 (sharedPreferences 사용) */
    private fun initScanner() {
        codeScanner = CodeScanner(this,binding.scannerQRCode).apply {
            camera = if(Settings.getInstance(requireContext()).isBackCamera) {
                CodeScanner.CAMERA_BACK
            } else {
                CodeScanner.CAMERA_FRONT
            }
            formats = listOf(BarcodeFormat.QR_CODE)
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isTouchFocusEnabled = false

            // TODO : handleScannedBarcode 구현, showError dialog 구현
            // decodeCallback = DecodeCallback(::handleScannedBarcode)
            // errorCallback = ErrorCallback(::showError)
        }
    }

    override fun onBarcodeConfirmed(barcode: Barcode) {
        handleConfirmedBarcode(barcode)
    }

    override fun onBarcodeDeclined() {
        restartPreview()
    }

    private fun handleConfirmedBarcode(barcode: Barcode) {

    }

    private fun restartPreview() {
        this@QrScanActivity.runOnUiThread {
            codeScanner.startPreview()
        }
    }
}