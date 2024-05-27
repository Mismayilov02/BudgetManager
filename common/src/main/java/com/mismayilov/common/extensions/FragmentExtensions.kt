package com.mismayilov.common.extensions

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.mismayilov.common.R
import com.mismayilov.common.databinding.CustomDialogDesignBinding

fun Fragment.showDialog(
    message: String? = null,
    isCancelable: Boolean = true,
    positiveButton: (()->Unit)? = null,
    negativeButton: (()->Unit)? = null,
) {
    val dialog = Dialog(requireContext())
    val binding: CustomDialogDesignBinding = CustomDialogDesignBinding.inflate(layoutInflater)
    dialog.setContentView(binding.root)
    dialog.setCancelable(isCancelable)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.show()

    message?.let {
        binding.dialogMessageTxt.text = it
    }
    binding.btnYes.setOnClickListener {
        positiveButton?.invoke()
        dialog.dismiss()
    }
    binding.btnNo.setOnClickListener {
        negativeButton?.invoke()
        dialog.dismiss()
    }
}

fun Fragment.showToast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), text, length).show()
}