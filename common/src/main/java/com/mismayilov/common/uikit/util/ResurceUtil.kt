package com.mismayilov.uikit.util

import android.content.Context


fun getResourceIdByName(context: Context, name: String, defType:String = "drawable"): Int {
   return context.resources.getIdentifier(name, defType, context.packageName)
}