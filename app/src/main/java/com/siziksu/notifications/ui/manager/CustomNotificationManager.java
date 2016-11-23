package com.siziksu.notifications.ui.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.siziksu.notifications.R;
import com.siziksu.notifications.broadcast.NotificationReceiver;
import com.siziksu.notifications.common.Utils;
import com.siziksu.notifications.ui.activity.MainActivity;

public class CustomNotificationManager {

    private static final int NOTIFICATION_ID = 1352;
    private static final int NOTIFICATION_REQUEST_CODE_CANCEL = 13520;
    private static final int NOTIFICATION_REQUEST_CODE_DISMISS = 13521;
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
        PendingIntent pending = getPendingIntent(sticky);
        PendingIntent pendingDismiss = getPendingReceiver(NOTIFICATION_REQUEST_CODE_DISMISS, NOTIFICATION_ID, NotificationReceiver.ACTION_DISMISS);
        NotificationCompat.Builder builder = getBuilder(remoteViews, bigRemoteViews, pending, pendingDismiss);
        Notification notification = getNotification(sticky, builder);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @NonNull
    private Notification getNotification(boolean sticky, NotificationCompat.Builder builder) {
        Notification notification = builder.build();
        if (sticky) {
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        } else {
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }
        return notification;
    }

    private NotificationCompat.Builder getBuilder(RemoteViews remoteViews, RemoteViews bigRemoteViews, PendingIntent pending, PendingIntent pendingDismiss) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setSound(sound)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigRemoteViews)
                .setContentIntent(pending)
                .setDeleteIntent(pendingDismiss);
    }

    private PendingIntent getPendingIntent(boolean sticky) {
        TaskStackBuilder stack = TaskStackBuilder.create(context);
        stack.addParentStack(MainActivity.class);
        stack.addNextIntent(new Intent(context, MainActivity.class));
        PendingIntent pending;
        if (!sticky) {
            pending = stack.getPendingIntent(NOTIFICATION_REQUEST_CODE_CANCEL, PendingIntent.FLAG_ONE_SHOT);
        } else {
            pending = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_CODE_CANCEL, new Intent(), NO_FLAGS);
        }
        return pending;
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
        PendingIntent pendingCancel = getPendingReceiver(NOTIFICATION_REQUEST_CODE_CANCEL, NOTIFICATION_ID, NotificationReceiver.ACTION_CANCEL);
        remoteViews.setOnClickPendingIntent(R.id.notificationCancel, pendingCancel);
        remoteViews.setImageViewResource(R.id.notificationEndIcon, endIconResource);
        return remoteViews;
    }

    @NonNull
    private PendingIntent getPendingReceiver(int requestCode, int notificationId, String action) {
        Intent receiver = new Intent(NotificationReceiver.INTENT);
        receiver.putExtra(NotificationReceiver.EXTRA_ID, notificationId);
        receiver.putExtra(NotificationReceiver.EXTRA_ACTION, action);
        return PendingIntent.getBroadcast(context, requestCode, receiver, NO_FLAGS);
    }
}
