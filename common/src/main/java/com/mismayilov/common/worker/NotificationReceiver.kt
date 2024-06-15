package com.mismayilov.common.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mismayilov.common.helpers.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationHelper: NotificationHelper
    override fun onReceive(context: Context, intent: Intent) {
        notificationHelper.showNotification("Reminder", "Don't forget to record your daily income and expenses.")
    }
}