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

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.siziksu.notifications.R;
import com.siziksu.notifications.broadcast.NotificationReceiver;
import com.siziksu.notifications.common.Constants;
import com.siziksu.notifications.common.Preferences;
import com.siziksu.notifications.common.Utils;

import java.util.ArrayList;
import java.util.List;

final class InboxStyleNotification {

    private static final int NO_FLAGS = 0;

    private final Context context;
    private List<Message> messages = new ArrayList<>();

    InboxStyleNotification(Context context) {
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
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setLargeIcon(icon)
                .setSound(sound)
                .setColor(Color.GREEN)
                .addAction(R.drawable.ic_close_14dp, context.getString(R.string.action_cancel), pendingCancel)
                .setContentIntent(pending)
                .setDeleteIntent(pendingDismiss);
        getNotificationContent(builder);
        return builder;
    }

    private void getNotificationContent(NotificationCompat.Builder builder) {
        if (messages.size() == 1) {
            builder.setContentTitle((messages.get(0).owner));
            builder.setContentText(messages.get(0).message);
        } else {
            builder.setContentTitle(messages.size() + context.getString(R.string.new_messages_suffix));
            builder.setContentText(context.getString(R.string.you_have_new_messages));
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(messages.size() + context.getString(R.string.new_messages_suffix));
            if (messages.size() > 3) {
                inboxStyle.setSummaryText("+" + (messages.size() - 3) + context.getString(R.string.more_suffix));
            }
            for (int i = 0; i < messages.size(); i++) {
                if (i == 3) {
                    break;
                }
                inboxStyle.addLine(Utils.getInboxSpannable(messages.get(i).owner, messages.get(i).message));
            }
            if (messages.size() > 3) {
                inboxStyle.addLine("...");
            }
            builder.setStyle(inboxStyle);
        }
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

    void addMessage() {
        String notifications = Preferences.get().getString(Constants.PREFERENCES_NOTIFICATIONS_KEY, "");
        List<Message> list = new Gson().fromJson(notifications, new TypeToken<List<Message>>() {}.getType());
        messages.clear();
        if (list != null) {
            messages.addAll(list);
        }
        messages.add(new Message(Constants.FAKE_USER, Constants.INBOX_FAKE_MESSAGE));
        String notificationsToTrack = new Gson().toJson(messages);
        Preferences.get().setString(Constants.PREFERENCES_NOTIFICATIONS_KEY, notificationsToTrack);
    }

    void clearMessages() {
        Preferences.get().deleteKey(Constants.PREFERENCES_NOTIFICATIONS_KEY);
    }

    private class Message {

        @SerializedName("owner")
        @Expose
        String owner;
        @SerializedName("message")
        @Expose
        String message;

        Message(String owner, String message) {
            this.owner = owner;
            this.message = message;
        }
    }
}
