package com.mismayilov.common.utility

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun showDatePicker(context: Context, onDateSelected: (Long) -> Unit) {
    val calendar = Calendar.getInstance()
    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        onDateSelected(calendar.timeInMillis)
    }
    DatePickerDialog(
        context,
        dateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun showTimePicker(context: Context, onTimeSelected: (Long) -> Unit, onCancel: () -> Unit = {}) {
    val calendar = Calendar.getInstance()
    val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        onTimeSelected(calendar.timeInMillis)
    }
    val timePickerDialog = TimePickerDialog(
        context,
        timeSetListener,
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    timePickerDialog.setOnCancelListener {
        onCancel()
    }

    timePickerDialog.show()
}