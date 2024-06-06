package com.mismayilov.common.extensions

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun Double.toCurrencyString(): String {
   if (this % 1 == 0.0) return String.format("%.0f", this)
    return String.format("%.2f", this)
}