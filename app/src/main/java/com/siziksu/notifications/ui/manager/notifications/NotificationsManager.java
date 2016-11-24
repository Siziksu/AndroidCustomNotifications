package com.siziksu.notifications.ui.manager.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.siziksu.notifications.R;
import com.siziksu.notifications.broadcast.NotificationReceiver;
import com.siziksu.notifications.common.Constants;
import com.siziksu.notifications.common.Utils;
import com.siziksu.notifications.ui.activity.MainActivity;

public class NotificationsManager {

    private static final int NOTIFICATION_ID = 1147;
    private static final int REQUEST_CODE_CANCEL = 11470;
    private static final int REQUEST_CODE_DISMISS = 11471;
    private static final int INBOX_NOTIFICATION_ID = 1845;
    private static final int INBOX_REQUEST_CODE_CANCEL = 18450;
    private static final int INBOX_REQUEST_CODE_DISMISS = 18451;
    private static final int CUSTOM_NOTIFICATION_ID = 1352;
    private static final int CUSTOM_REQUEST_CODE_CANCEL = 13520;
    private static final int CUSTOM_REQUEST_CODE_DISMISS = 13521;

    private final NormalNotification manager;
    private final InboxStyleNotification inboxManager;
    private final CustomNotification custom;

    public NotificationsManager(Context context) {
        this.manager = new NormalNotification(context);
        this.inboxManager = new InboxStyleNotification(context);
        this.custom = new CustomNotification(context);
    }

    public void showNotification(boolean sticky) {
        PendingIntent pending = manager.getPendingIntent(REQUEST_CODE_CANCEL, MainActivity.class, sticky);
        PendingIntent pendingCancel = manager.getPendingReceiver(REQUEST_CODE_CANCEL, NOTIFICATION_ID, NotificationReceiver.ACTION_CANCEL);
        PendingIntent pendingDismiss = manager.getPendingReceiver(REQUEST_CODE_DISMISS, NOTIFICATION_ID, NotificationReceiver.ACTION_DISMISS);
        NotificationCompat.Builder builder = manager.getBuilder(pending, pendingCancel, pendingDismiss);
        Notification notification = manager.getNotification(sticky, builder);
        manager.publish(NOTIFICATION_ID, notification);
    }

    public void showInboxNotification(boolean sticky) {
        inboxManager.addMessage();
        PendingIntent pending = inboxManager.getPendingIntent(INBOX_REQUEST_CODE_CANCEL, MainActivity.class, sticky);
        PendingIntent pendingCancel = inboxManager.getPendingReceiver(INBOX_REQUEST_CODE_CANCEL, INBOX_NOTIFICATION_ID, NotificationReceiver.ACTION_CANCEL);
        PendingIntent pendingDismiss = inboxManager.getPendingReceiver(INBOX_REQUEST_CODE_DISMISS, INBOX_NOTIFICATION_ID, NotificationReceiver.ACTION_DISMISS);
        NotificationCompat.Builder builder = inboxManager.getBuilder(pending, pendingCancel, pendingDismiss);
        Notification notification = inboxManager.getNotification(sticky, builder);
        inboxManager.publish(INBOX_NOTIFICATION_ID, notification);
    }

    public void clearMessages() {
        inboxManager.clearMessages();
    }

    public void showCustomNotification(boolean sticky) {
        PendingIntent pending = custom.getPendingIntent(CUSTOM_REQUEST_CODE_CANCEL, MainActivity.class, sticky);
        PendingIntent pendingCancel = custom.getPendingReceiver(CUSTOM_REQUEST_CODE_CANCEL, CUSTOM_NOTIFICATION_ID, NotificationReceiver.ACTION_CANCEL);
        PendingIntent pendingDismiss = custom.getPendingReceiver(CUSTOM_REQUEST_CODE_DISMISS, CUSTOM_NOTIFICATION_ID, NotificationReceiver.ACTION_DISMISS);
        RemoteViews remoteViews = custom.getRemoteViews(
                R.layout.notification,
                R.drawable.ic_phone_call_24dp,
                Constants.FAKE_USER,
                Utils.getSpannable()
        );
        RemoteViews bigRemoteViews = custom.getBigRemoteViews(
                R.layout.notification_big,
                R.drawable.ic_phone_call_48dp,
                Constants.FAKE_USER,
                Utils.getSpannable(),
                R.drawable.ic_exclamation_48dp,
                pendingCancel
        );
        NotificationCompat.Builder builder = custom.getBuilder(remoteViews, bigRemoteViews, pending, pendingDismiss);
        Notification notification = custom.getNotification(sticky, builder);
        custom.publish(CUSTOM_NOTIFICATION_ID, notification);
    }
}
