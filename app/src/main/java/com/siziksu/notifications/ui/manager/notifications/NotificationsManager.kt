package com.siziksu.notifications.ui.manager.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.siziksu.notifications.R
import com.siziksu.notifications.broadcast.NotificationReceiver
import com.siziksu.notifications.common.Constants
import com.siziksu.notifications.common.Utils
import com.siziksu.notifications.ui.activity.MainActivity

class NotificationsManager(val context: Context) {

    private val manager: NormalNotification = NormalNotification(context)
    private val inboxManager: InboxStyleNotification = InboxStyleNotification(context)
    private val custom: CustomNotification = CustomNotification(context)

    fun showNotification(sticky: Boolean) {
        val pending = manager.getPendingIntent(REQUEST_CODE_CANCEL, MainActivity::class.java, sticky)
        val pendingCancel = manager.getPendingReceiver(REQUEST_CODE_CANCEL, NOTIFICATION_ID, NotificationReceiver.ACTION_CANCEL)
        val pendingDismiss = manager.getPendingReceiver(REQUEST_CODE_DISMISS, NOTIFICATION_ID, NotificationReceiver.ACTION_DISMISS)
        val builder = manager.getBuilder(pending!!, pendingCancel, pendingDismiss)
        val notification = manager.getNotification(sticky, builder)
        setNotificationChannels()
        manager.publish(NOTIFICATION_ID, notification)
    }

    fun showInboxNotification(sticky: Boolean) {
        inboxManager.addMessage()
        val pending = inboxManager.getPendingIntent(INBOX_REQUEST_CODE_CANCEL, MainActivity::class.java, sticky)
        val pendingCancel = inboxManager.getPendingReceiver(INBOX_REQUEST_CODE_CANCEL, INBOX_NOTIFICATION_ID, NotificationReceiver.ACTION_CANCEL)
        val pendingDismiss = inboxManager.getPendingReceiver(INBOX_REQUEST_CODE_DISMISS, INBOX_NOTIFICATION_ID, NotificationReceiver.ACTION_DISMISS)
        val builder = inboxManager.getBuilder(pending!!, pendingCancel, pendingDismiss)
        val notification = inboxManager.getNotification(sticky, builder)
        setNotificationChannels()
        inboxManager.publish(INBOX_NOTIFICATION_ID, notification)
    }

    fun clearMessages() {
        inboxManager.clearMessages()
    }

    fun showCustomNotification(sticky: Boolean) {
        val pending = custom.getPendingIntent(CUSTOM_REQUEST_CODE_CANCEL, MainActivity::class.java, sticky)
        val pendingCancel = custom.getPendingReceiver(CUSTOM_REQUEST_CODE_CANCEL, CUSTOM_NOTIFICATION_ID, NotificationReceiver.ACTION_CANCEL)
        val pendingDismiss = custom.getPendingReceiver(CUSTOM_REQUEST_CODE_DISMISS, CUSTOM_NOTIFICATION_ID, NotificationReceiver.ACTION_DISMISS)
        val remoteViews = custom.getRemoteViews(
            R.layout.notification,
            R.drawable.ic_phone_call_24dp,
            Constants.FAKE_USER,
            Utils.spannable
        )
        val bigRemoteViews = custom.getBigRemoteViews(
            R.layout.notification_big,
            R.drawable.ic_phone_call_48dp,
            Constants.FAKE_USER,
            Utils.spannable,
            R.drawable.ic_exclamation_48dp,
            pendingCancel
        )
        val builder = custom.getBuilder(remoteViews, bigRemoteViews, pending!!, pendingDismiss)
        val notification = custom.getNotification(sticky, builder)
        setNotificationChannels()
        custom.publish(CUSTOM_NOTIFICATION_ID, notification)
    }

    private fun setNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.getNotificationChannel(Constants.CHANNEL_1_ID).let {
                val channel1 = NotificationChannel(Constants.CHANNEL_1_ID, Constants.CHANNEL_1_NAME, NotificationManager.IMPORTANCE_LOW)
                channel1.description = Constants.CHANNEL_1_DESCRIPTION
                channel1.enableLights(false)
                channel1.enableVibration(false)
                notificationManager.createNotificationChannel(channel1)
            }
        }
    }

    companion object {

        private const val NOTIFICATION_ID = 1147
        private const val REQUEST_CODE_CANCEL = 11470
        private const val REQUEST_CODE_DISMISS = 11471
        private const val INBOX_NOTIFICATION_ID = 1845
        private const val INBOX_REQUEST_CODE_CANCEL = 18450
        private const val INBOX_REQUEST_CODE_DISMISS = 18451
        private const val CUSTOM_NOTIFICATION_ID = 1352
        private const val CUSTOM_REQUEST_CODE_CANCEL = 13520
        private const val CUSTOM_REQUEST_CODE_DISMISS = 13521
    }
}
