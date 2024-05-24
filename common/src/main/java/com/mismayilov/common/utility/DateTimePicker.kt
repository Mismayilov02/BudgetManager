package com.mismayilov.common.utility

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun showDatePicker(context:Context, lambda: (String,String,String) -> Unit) {
    val calendar = Calendar.getInstance()
    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val day:String  = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        val month:String  = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
        val year:String  = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)
        lambda(day,month,year)
    }
    DatePickerDialog(
        context,
        dateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}