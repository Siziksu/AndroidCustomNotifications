package com.siziksu.notifications.ui.manager.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.siziksu.notifications.R;
import com.siziksu.notifications.broadcast.NotificationReceiver;
import com.siziksu.notifications.common.Constants;
import com.siziksu.notifications.common.Utils;

final class NormalNotification {

    private static final int NO_FLAGS = 0;

    private final Context context;

    NormalNotification(Context context) {
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

    NotificationCompat.Builder getBuilder(PendingIntent pending, PendingIntent pendingCancel, PendingIntent pendingDismiss) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle().bigText(Utils.getSpannable());
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setLargeIcon(icon)
                .setContentTitle(Constants.FAKE_USER)
                .setContentText(Utils.getSpannable())
                .setStyle(style)
                .setSound(sound)
                .setColor(Color.GREEN)
                .addAction(R.drawable.ic_close_24dp, context.getString(R.string.action_dismiss), pendingCancel)
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
}
