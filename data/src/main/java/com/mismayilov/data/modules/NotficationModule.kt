package com.mismayilov.data.modules

import android.content.Context
import com.mismayilov.common.helpers.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotficationModule {

        @Provides
        @Singleton
        fun provideNotificationHelper(
            @ApplicationContext context: Context
        ): NotificationHelper {
            return NotificationHelper(context)
        }
}