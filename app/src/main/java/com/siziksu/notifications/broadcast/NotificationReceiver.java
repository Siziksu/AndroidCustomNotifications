package com.siziksu.notifications.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.siziksu.notifications.common.Constants;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String INTENT = "notification.ACTION_LISTENER";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_ACTION = "action";
    public static final String ACTION_CANCEL = "action_cancel";
    public static final String ACTION_DISMISS = "action_dismiss";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(NotificationReceiver.EXTRA_ID) && extras.containsKey(NotificationReceiver.EXTRA_ACTION)) {
            int id = extras.getInt(NotificationReceiver.EXTRA_ID);
            String action = extras.getString(NotificationReceiver.EXTRA_ACTION);
            useExtras(context, id, action);
        }
    }

    private void useExtras(Context context, int id, String action) {
        if (action != null) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            switch (action) {
                case NotificationReceiver.ACTION_CANCEL:
                    manager.cancel(id);
                    break;
                case NotificationReceiver.ACTION_DISMISS:
                    Log.i(Constants.TAG, "Notification: " + id + " dismissed");
                    break;
                default:
                    break;
            }
        }
    }
}
