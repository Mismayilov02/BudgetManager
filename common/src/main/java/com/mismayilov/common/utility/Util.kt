package com.mismayilov.common.utility

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class Util {
    companion object {
        @SuppressLint("SimpleDateFormat")
        val simpleDateFormat:SimpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss")
    }
}