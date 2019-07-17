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
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.siziksu.notifications.R
import com.siziksu.notifications.broadcast.NotificationReceiver
import com.siziksu.notifications.common.Constants
import com.siziksu.notifications.common.Preferences
import com.siziksu.notifications.common.Utils
import java.util.ArrayList

internal class InboxStyleNotification(private val context: Context) {

    private val messages = ArrayList<Message>()
    private val preferences = Preferences(context)

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
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_phone_call_24dp)
            .setLargeIcon(icon)
            .setSound(sound)
            .setColor(Color.GREEN)
            .addAction(R.drawable.ic_close_24dp, context.getString(R.string.action_dismiss), pendingCancel)
            .setContentIntent(pending)
            .setDeleteIntent(pendingDismiss)
        getNotificationContent(builder)
        return builder
    }

    private fun getNotificationContent(builder: NotificationCompat.Builder) {
        if (messages.size == 1) {
            builder.setContentTitle(messages[0].owner)
            builder.setContentText(messages[0].message)
        } else {
            builder.setContentTitle(messages.size.toString() + context.getString(R.string.new_messages_suffix))
            builder.setContentText(context.getString(R.string.you_have_new_messages))
            val inboxStyle = NotificationCompat.InboxStyle()
            inboxStyle.setBigContentTitle(messages.size.toString() + context.getString(R.string.new_messages_suffix))
            if (messages.size > 3) {
                inboxStyle.setSummaryText("+" + (messages.size - 3) + context.getString(R.string.more_suffix))
            }
            for (i in messages.indices) {
                if (i == 3) {
                    break
                }
                inboxStyle.addLine(Utils.getInboxSpannable(messages[i].owner, messages[i].message))
            }
            if (messages.size > MAX_MESSAGES_DISPLAYED) {
                inboxStyle.addLine("...")
            }
            builder.setStyle(inboxStyle)
        }
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
        val receiver = Intent(context, NotificationReceiver::class.java)
        receiver.putExtra(NotificationReceiver.EXTRA_ID, notificationId)
        receiver.putExtra(NotificationReceiver.EXTRA_ACTION, action)
        return PendingIntent.getBroadcast(context, requestCode, receiver, NO_FLAGS)
    }

    fun publish(notificationId: Int, notification: Notification) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notification)
    }

    fun addMessage() {
        val notifications = preferences.getString(Constants.PREFERENCES_NOTIFICATIONS_KEY, "")
        val list = Gson().fromJson<List<Message>>(notifications, object : TypeToken<List<Message>>() {}.type)
        messages.clear()
        if (list != null) {
            messages.addAll(list)
        }
        messages.add(Message(Constants.FAKE_USER, Constants.INBOX_FAKE_MESSAGE))
        val notificationsToTrack = Gson().toJson(messages)
        preferences.setString(Constants.PREFERENCES_NOTIFICATIONS_KEY, notificationsToTrack)
    }

    fun clearMessages() {
        preferences.deleteKey(Constants.PREFERENCES_NOTIFICATIONS_KEY)
    }

    private inner class Message internal constructor(
        @field:SerializedName("owner")
        @field:Expose
        internal var owner: String, @field:SerializedName("message")
        @field:Expose
        internal var message: String
    )

    companion object {

        private const val NO_FLAGS = 0
        private const val MAX_MESSAGES_DISPLAYED = 3
    }
}
