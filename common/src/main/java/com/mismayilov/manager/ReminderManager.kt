package com.mismayilov.manager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mismayilov.common.R
import com.mismayilov.common.helpers.NotificationHelper
import com.mismayilov.worker.ReminderBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class ReminderManager @Inject constructor(
    private val alarmManager: AlarmManager,
    private val notificationHelper: NotificationHelper,
    private val context: Context
) {

    private var pendingIntent: PendingIntent? = null

    fun scheduleDailyReminder(hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelDailyReminder() {
        pendingIntent?.run {
            alarmManager.cancel(this)
            cancel()
            pendingIntent = null
        }
    }

    fun showNotification(message: String) {
        notificationHelper.showNotification("Başlık", message)
    }
}