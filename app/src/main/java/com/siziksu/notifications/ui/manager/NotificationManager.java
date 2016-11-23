package com.siziksu.notifications.ui.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.siziksu.notifications.R;
import com.siziksu.notifications.broadcast.NotificationReceiver;
import com.siziksu.notifications.common.Utils;
import com.siziksu.notifications.ui.activity.MainActivity;

public class NotificationManager {

    private static final int NOTIFICATION_ID = 1147;
    private static final int NOTIFICATION_REQUEST_CODE_CANCEL = 11470;
    private static final int NOTIFICATION_REQUEST_CODE_DISMISS = 11471;
    private static final int NO_FLAGS = 0;
    private static final int NO_ICON = 0;

    private final Context context;

    public NotificationManager(Context context) {
        this.context = context;
    }

    public void showNotification(boolean sticky) {
        PendingIntent pending = getPendingCancelIntent(sticky);
        PendingIntent pendingCancel = getPendingReceiver(NOTIFICATION_REQUEST_CODE_CANCEL, NOTIFICATION_ID, NotificationReceiver.ACTION_CANCEL);
        PendingIntent pendingDismiss = getPendingReceiver(NOTIFICATION_REQUEST_CODE_DISMISS, NOTIFICATION_ID, NotificationReceiver.ACTION_DISMISS);
        NotificationCompat.Builder builder = getBuilder(pending, pendingCancel, pendingDismiss);
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

    private NotificationCompat.Builder getBuilder(PendingIntent pending, PendingIntent pendingCancel, PendingIntent pendingDismiss) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle().bigText(Utils.getSpannable());
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setLargeIcon(icon)
                .setContentTitle(context.getString(R.string.jane_doe))
                .setContentText(Utils.getSpannable())
                .setStyle(style)
                .setSound(sound)
                .setColor(Color.GREEN)
                .addAction(NO_ICON, context.getString(R.string.action_cancel), pendingCancel)
                .setContentIntent(pending)
                .setDeleteIntent(pendingDismiss);
    }

    @NonNull
    private PendingIntent getPendingReceiver(int requestCode, int notificationId, String action) {
        Intent receiver = new Intent(NotificationReceiver.INTENT);
        receiver.putExtra(NotificationReceiver.EXTRA_ID, notificationId);
        receiver.putExtra(NotificationReceiver.EXTRA_ACTION, action);
        return PendingIntent.getBroadcast(context, requestCode, receiver, NO_FLAGS);
    }

    private PendingIntent getPendingCancelIntent(boolean sticky) {
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
}
