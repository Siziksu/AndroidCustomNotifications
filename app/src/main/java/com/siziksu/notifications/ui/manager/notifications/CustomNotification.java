package com.siziksu.notifications.ui.manager.notifications;

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
import com.siziksu.notifications.broadcast.NotificationReceiver;
import com.siziksu.notifications.common.Utils;

final class CustomNotification {

    private static final int NO_FLAGS = 0;

    private final Context context;

    CustomNotification(Context context) {
        this.context = context;
    }

    Notification getNotification(boolean sticky, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        if (sticky) {
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        } else {
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }
        return notification;
    }

    NotificationCompat.Builder getBuilder(RemoteViews remoteViews, RemoteViews bigRemoteViews, PendingIntent pending, PendingIntent pendingDismiss) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setSound(sound)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigRemoteViews)
                .setContentIntent(pending)
                .setDeleteIntent(pendingDismiss);
    }

    PendingIntent getPendingIntent(int requestCode, Class<?> clazz, boolean sticky) {
        PendingIntent pending;
        if (!sticky) {
            TaskStackBuilder stack = TaskStackBuilder.create(context);
            stack.addParentStack(clazz);
            stack.addNextIntent(new Intent(context, clazz));
            pending = stack.getPendingIntent(requestCode, PendingIntent.FLAG_ONE_SHOT);
        } else {
            pending = PendingIntent.getActivity(context, requestCode, new Intent(), NO_FLAGS);
        }
        return pending;
    }

    PendingIntent getPendingReceiver(int requestCode, int notificationId, String action) {
        Intent receiver = new Intent(NotificationReceiver.INTENT);
        receiver.putExtra(NotificationReceiver.EXTRA_ID, notificationId);
        receiver.putExtra(NotificationReceiver.EXTRA_ACTION, action);
        return PendingIntent.getBroadcast(context, requestCode, receiver, NO_FLAGS);
    }

    void publish(int notificationId, Notification notification) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notification);
    }

    RemoteViews getRemoteViews(int layout, int iconResource, String title, CharSequence text) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);
        remoteViews.setImageViewResource(R.id.notificationIcon, iconResource);
        remoteViews.setTextViewText(R.id.notificationTitle, title);
        remoteViews.setTextViewText(R.id.notificationText, text);
        remoteViews.setTextViewText(R.id.notificationTime, Utils.getTime());
        return remoteViews;
    }

    RemoteViews getBigRemoteViews(int layout, int iconResource, String title, CharSequence text, int endIconResource, PendingIntent pendingCancel) {
        RemoteViews remoteViews = getRemoteViews(layout, iconResource, title, text);
        remoteViews.setOnClickPendingIntent(R.id.notificationCancel, pendingCancel);
        remoteViews.setImageViewResource(R.id.notificationEndIcon, endIconResource);
        return remoteViews;
    }
}
