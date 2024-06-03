package com.mismayilov.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mismayilov.manager.ReminderManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderManager: ReminderManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Cihaz yeniden başlatıldığında hatırlatıcıyı tekrar ayarla
            reminderManager.scheduleDailyReminder(13, 0)
        }else{
            reminderManager.showNotification("Budget Manager")
        }
    }
}