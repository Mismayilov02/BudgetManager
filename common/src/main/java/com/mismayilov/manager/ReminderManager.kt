package com.mismayilov.manager

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mismayilov.common.worker.ReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class ReminderManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .build()
    fun scheduleDailyReminder(time:Long) {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val dailyWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .addTag(ReminderWorker::class.java.simpleName)
            .setConstraints(constraints)
            .setInitialDelay(calendar.timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueue(dailyWorkRequest)
    }

    fun cancelDailyReminder() {
        WorkManager.getInstance(context).cancelAllWorkByTag(ReminderWorker::class.java.simpleName)
    }
}