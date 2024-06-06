package com.mismayilov.common.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mismayilov.common.helpers.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
//    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
//        notificationHelper.showNotification("Hatırlatma", "Bu günlük hatırlatıcı mesajınız.")
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification("Hatırlatma", "Bu günlük hatırlatıcı mesajınız.")
        println("ReminderWorker.doWork")
        return Result.success()
    }
}