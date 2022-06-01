package com.mirim.refrigerator.view.ingredient

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mirim.refrigerator.databinding.ActivityHomeBinding
import com.mirim.refrigerator.databinding.ActivityQrScanBinding
import com.mirim.refrigerator.dialog.ConfirmBarcodeScanDialog
import com.mirim.refrigerator.model.Barcode

class QrScanActivity : AppCompatActivity(), ConfirmBarcodeScanDialog.Listener {

    private lateinit var binding: ActivityQrScanBinding

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

    override fun onBarcodeConfirmed(barcode: Barcode) {
        TODO("Not yet implemented")
    }

    override fun onBarcodeDeclined() {
        TODO("Not yet implemented")
    }
}