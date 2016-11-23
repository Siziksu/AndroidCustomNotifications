package com.siziksu.notifications.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationListener extends BroadcastReceiver {

    public static final String INTENT = "notification.ACTION_LISTENER";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_ACTION = "action";
    public static final String ACTION_CANCEL = "action_cancel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(NotificationListener.EXTRA_ID) && extras.containsKey(NotificationListener.EXTRA_ACTION)) {
            int id = extras.getInt(NotificationListener.EXTRA_ID);
            String action = extras.getString(NotificationListener.EXTRA_ACTION);
            useExtras(context, id, action);
        }
    }

    private void useExtras(Context context, int id, String action) {
        if (action != null) {
            switch (action) {
                case NotificationListener.ACTION_CANCEL:
                    NotificationManagerCompat manager = NotificationManagerCompat.from(context);
                    manager.cancel(id);
                    break;
                default:
                    break;
            }
        }
    }
}
