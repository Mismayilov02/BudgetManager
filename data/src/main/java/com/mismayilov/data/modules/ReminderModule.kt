package com.mismayilov.data.modules

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.mismayilov.common.helpers.NotificationHelper
import com.mismayilov.manager.ReminderManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReminderModule {

    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }


    @Provides
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }
    @Provides
    @Singleton
    fun provideReminderManager(
        alarmManager: AlarmManager,
        notificationHelper: NotificationHelper,
        @ApplicationContext context: Context
    ): ReminderManager {
        return ReminderManager(alarmManager, notificationHelper, context)
    }
}
