package com.mirim.refrigerator.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.mirim.refrigerator.R

class QRErrorDialog : DialogFragment() {

    interface Listener {
        fun onErrorDialogPositiveButtonClicked()
    }

    companion object {
        private const val ERROR_MESSAGE_KEY = "ERROR_MESSAGE_KEY"

        fun newInstance(context: Context, error: Throwable?): QRErrorDialog {
            return QRErrorDialog().apply {
                arguments = Bundle().apply {
                    putString(ERROR_MESSAGE_KEY, getErrorMessage(context, error))
                }
                isCancelable = false
            }
        }

        private fun getErrorMessage(context: Context, error: Throwable?): String {
            return error?.message ?: "Something went wrong"
        }
    }

    private var listener: Listener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = arguments?.getString(ERROR_MESSAGE_KEY).orEmpty()

        val dialog = AlertDialog.Builder(requireActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert)
            .setTitle("Error")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> listener?.onErrorDialogPositiveButtonClicked() }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
        }

        return dialog
    }



}