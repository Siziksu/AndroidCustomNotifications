package com.siziksu.notifications.ui.manager.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import android.widget.RemoteViews
import com.siziksu.notifications.R
import com.siziksu.notifications.broadcast.NotificationReceiver
import com.siziksu.notifications.common.Constants.Companion.CHANNEL_1_ID
import com.siziksu.notifications.common.Utils

internal class CustomNotification(private val context: Context) {

    fun getNotification(sticky: Boolean, builder: NotificationCompat.Builder): Notification {
        val notification = builder.build()
        if (sticky) {
            notification.flags = notification.flags or (Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT)
        } else {
            notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        }
        return notification
    }

    fun getBuilder(remoteViews: RemoteViews, bigRemoteViews: RemoteViews, pending: PendingIntent, pendingDismiss: PendingIntent): NotificationCompat.Builder {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return NotificationCompat.Builder(context, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_phone_call_24dp)
            .setSound(sound)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(bigRemoteViews)
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

    fun getRemoteViews(layout: Int, iconResource: Int, title: String, text: CharSequence): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, layout)
        remoteViews.setImageViewResource(R.id.notificationIcon, iconResource)
        remoteViews.setTextViewText(R.id.notificationTitle, title)
        remoteViews.setTextViewText(R.id.notificationText, text)
        remoteViews.setTextViewText(R.id.notificationTime, Utils.time)
        return remoteViews
    }

    fun getBigRemoteViews(layout: Int, iconResource: Int, title: String, text: CharSequence, endIconResource: Int, pendingCancel: PendingIntent): RemoteViews {
        val remoteViews = getRemoteViews(layout, iconResource, title, text)
        remoteViews.setOnClickPendingIntent(R.id.notificationCancel, pendingCancel)
        remoteViews.setImageViewResource(R.id.notificationEndIcon, endIconResource)
        return remoteViews
    }

    companion object {

        private const val NO_FLAGS = 0
    }
}
