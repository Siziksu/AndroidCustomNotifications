package com.siziksu.notifications.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import com.siziksu.notifications.common.Logs

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras
        extras?.let {
            if (extras.containsKey(EXTRA_ID) && extras.containsKey(EXTRA_ACTION)) {
                val id = extras.getInt(EXTRA_ID)
                val action = extras.getString(EXTRA_ACTION) ?: ""
                useExtras(context, id, action)
            }
        }
    }

    private fun useExtras(context: Context, id: Int, action: String) {
        val manager = NotificationManagerCompat.from(context)
        when (action) {
            ACTION_CANCEL -> manager.cancel(id)
            ACTION_DISMISS -> Logs.print("Notification: $id dismissed")
            else -> Unit
        }
    }

    companion object {

        const val INTENT = "notification.ACTION_LISTENER"
        const val EXTRA_ID = "id"
        const val EXTRA_ACTION = "action"
        const val ACTION_CANCEL = "action_cancel"
        const val ACTION_DISMISS = "action_dismiss"
    }
}
