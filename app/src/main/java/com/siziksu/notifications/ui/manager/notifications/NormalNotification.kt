package com.siziksu.notifications.ui.manager.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import com.siziksu.notifications.R
import com.siziksu.notifications.broadcast.NotificationReceiver
import com.siziksu.notifications.common.Constants
import com.siziksu.notifications.common.Utils

internal class NormalNotification(private val context: Context) {

    fun getNotification(sticky: Boolean, builder: NotificationCompat.Builder): Notification {
        val notification = builder.build()
        if (sticky) {
            notification.flags = notification.flags or (Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT)
        } else {
            notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        }
        return notification
    }

    fun getBuilder(pending: PendingIntent, pendingCancel: PendingIntent, pendingDismiss: PendingIntent): NotificationCompat.Builder {
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.notification_icon)
        val style = NotificationCompat.BigTextStyle().bigText(Utils.spannable)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return NotificationCompat.Builder(context, Constants.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_phone_call_24dp)
            .setLargeIcon(icon)
            .setContentTitle(Constants.FAKE_USER)
            .setContentText(Utils.spannable)
            .setStyle(style)
            .setSound(sound)
            .setColor(Color.GREEN)
            .addAction(R.drawable.ic_close_24dp, context.getString(R.string.action_dismiss), pendingCancel)
            .setContentIntent(pending)
            .setDeleteIntent(pendingDismiss)
    }

    fun getPendingIntent(requestCode: Int, clazz: Class<*>, sticky: Boolean): PendingIntent? {
        val pending: PendingIntent?
        pending = if (!sticky) {
            val stack = TaskStackBuilder.create(context)
            stack.addParentStack(clazz)
            stack.addNextIntent(Intent(context, clazz))
            stack.getPendingIntent(requestCode, PendingIntent.FLAG_ONE_SHOT)
        } else {
            PendingIntent.getActivity(context, requestCode, Intent(), NO_FLAGS)
        }
        return pending
    }

    fun getPendingReceiver(requestCode: Int, notificationId: Int, action: String): PendingIntent {
        val receiver = Intent(NotificationReceiver.INTENT)
        receiver.putExtra(NotificationReceiver.EXTRA_ID, notificationId)
        receiver.putExtra(NotificationReceiver.EXTRA_ACTION, action)
        return PendingIntent.getBroadcast(context, requestCode, receiver, NO_FLAGS)
    }

    fun publish(notificationId: Int, notification: Notification) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notification)
    }

    companion object {

        private val NO_FLAGS = 0
    }
}
