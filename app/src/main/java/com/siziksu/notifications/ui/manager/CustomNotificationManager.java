package com.siziksu.notifications.ui.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.siziksu.notifications.R;
import com.siziksu.notifications.broadcast.NotificationListener;
import com.siziksu.notifications.common.Utils;
import com.siziksu.notifications.ui.activity.MainActivity;

public class CustomNotificationManager {

    private static final int CUSTOM_NOTIFICATION_ID = 1352;
    private static final int CUSTOM_NOTIFICATION_REQUEST_CODE = 1352;
    private static final int NO_FLAGS = 0;

    private final Context context;

    public CustomNotificationManager(Context context) {
        this.context = context;
    }

    public void showCustomNotification(boolean sticky) {
        RemoteViews remoteViews = getRemoteViews(
                context,
                R.layout.notification,
                R.drawable.ic_phone_call_24dp,
                context.getString(R.string.jane_doe),
                Utils.getSpannable()
        );
        RemoteViews bigRemoteViews = getBigRemoteViews(
                context,
                R.layout.notification_big,
                R.drawable.ic_phone_call_48dp,
                context.getString(R.string.jane_doe),
                Utils.getSpannable(),
                R.drawable.ic_exclamation_48dp
        );
        TaskStackBuilder stack = TaskStackBuilder.create(context);
        stack.addParentStack(MainActivity.class);
        stack.addNextIntent(new Intent(context, MainActivity.class));
        PendingIntent pending;
        if (!sticky) {
            pending = stack.getPendingIntent(CUSTOM_NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_ONE_SHOT);
        } else {
            pending = PendingIntent.getActivity(context, CUSTOM_NOTIFICATION_REQUEST_CODE, new Intent(), NO_FLAGS);
        }
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setSound(sound)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigRemoteViews)
                .setContentIntent(pending);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = builder.build();
        if (sticky) {
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        } else {
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }
        notificationManager.notify(CUSTOM_NOTIFICATION_ID, notification);
    }

    private RemoteViews getRemoteViews(Context context, int layout, int iconResource, String title, CharSequence text) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);
        remoteViews.setImageViewResource(R.id.notificationIcon, iconResource);
        remoteViews.setTextViewText(R.id.notificationTitle, title);
        remoteViews.setTextViewText(R.id.notificationText, text);
        remoteViews.setTextViewText(R.id.notificationTime, Utils.getTime());
        return remoteViews;
    }

    private RemoteViews getBigRemoteViews(Context context, int layout, int iconResource, String title, CharSequence text, int endIconResource) {
        RemoteViews remoteViews = getRemoteViews(context, layout, iconResource, title, text);
        Intent cancel = new Intent(NotificationListener.INTENT);
        cancel.putExtra(NotificationListener.EXTRA_ID, CUSTOM_NOTIFICATION_ID);
        cancel.putExtra(NotificationListener.EXTRA_ACTION, NotificationListener.ACTION_CANCEL);
        PendingIntent pending = PendingIntent.getBroadcast(context, CUSTOM_NOTIFICATION_REQUEST_CODE, cancel, NO_FLAGS);
        remoteViews.setOnClickPendingIntent(R.id.notificationCancel, pending);
        remoteViews.setImageViewResource(R.id.notificationEndIcon, endIconResource);
        return remoteViews;
    }
}
