package com.siziksu.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.siziksu.notifications.common.Constants

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setNotificationChannels()
    }

    private fun setNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(Constants.CHANNEL_1_ID, Constants.CHANNEL_1_NAME, NotificationManager.IMPORTANCE_LOW)
            channel1.description = Constants.CHANNEL_1_DESCRIPTION
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
        }
    }
}
