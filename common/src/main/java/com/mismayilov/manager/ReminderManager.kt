package com.mismayilov.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import com.mismayilov.common.worker.NotificationReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    private val notificationIntent: PendingIntent by lazy {
        val intent = Intent(context, NotificationReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun scheduleDailyReminder(time: Long) {
        val now = Calendar.getInstance()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = time

            if (before(now)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            notificationIntent
        )
    }

    fun cancelDailyReminder() {
        alarmManager.cancel(notificationIntent)
    }
}

