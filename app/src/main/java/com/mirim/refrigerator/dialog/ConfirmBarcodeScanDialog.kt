package com.mirim.refrigerator.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.R
import com.mirim.refrigerator.model.Barcode

class ConfirmBarcodeScanDialog : DialogFragment() {

    interface Listener {
        fun onBarcodeConfirmed(barcode : Barcode)
        fun onBarcodeDeclined()
    }

    /* static 객체 생성 */
    companion object {
        private const val BARCODE_KEY = "BARCODE_KEY"
        public fun newInstance(barcode : Barcode): ConfirmBarcodeScanDialog {
            return ConfirmBarcodeScanDialog().apply {
                arguments = Bundle().apply { putSerializable(BARCODE_KEY,barcode) }
                isCancelable = false
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = parentFragment as? Listener
        val barcode = arguments?.getSerializable(BARCODE_KEY) as? Barcode ?: throw IllegalArgumentException("No Barcode Passed")
        val dialog = AlertDialog.Builder(requireActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert)
            .setTitle("Confirm")
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                listener?.onBarcodeConfirmed(barcode)
            }
            .setNegativeButton("Cancel") { _, _ ->
                listener?.onBarcodeDeclined()
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.deep_deep_gray))
        }

        return dialog
    }


}