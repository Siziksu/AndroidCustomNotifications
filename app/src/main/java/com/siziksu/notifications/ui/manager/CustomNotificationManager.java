package com.siziksu.notifications.ui.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.RemoteViews;

import com.siziksu.notifications.R;
import com.siziksu.notifications.broadcast.NotificationListener;
import com.siziksu.notifications.common.constants.Constants;
import com.siziksu.notifications.common.constants.Utils;
import com.siziksu.notifications.ui.activity.MainActivity;

public class CustomNotificationManager {

    private static final int CUSTOM_NOTIFICATION_ID = 1352;
    private static final int CUSTOM_NOTIFICATION_REQUEST_CODE = 1352;
    private static final int NO_FLAGS = 0;

    private final Context context;

    public CustomNotificationManager(Context context) {
        this.context = context;
    }

    public void showCustomNotification() {
        RemoteViews remoteViews = getRemoteViews(
                context,
                R.layout.notification,
                R.drawable.ic_phone_call_24dp,
                context.getString(R.string.jane_doe),
                getSpannable()
        );
        RemoteViews bigRemoteViews = getBigRemoteViews(
                context,
                R.layout.notification_big,
                R.drawable.ic_phone_call_48dp,
                context.getString(R.string.jane_doe),
                getSpannable(),
                R.drawable.ic_exclamation_48dp
        );
        TaskStackBuilder stack = TaskStackBuilder.create(context);
        stack.addParentStack(MainActivity.class);
        stack.addNextIntent(new Intent(context, MainActivity.class));
        PendingIntent pending = stack.getPendingIntent(CUSTOM_NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setSound(sound)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigRemoteViews)
                .setContentIntent(pending)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = builder.build();
        notificationManager.notify(CUSTOM_NOTIFICATION_ID, notification);
    }

    @NonNull
    private Spannable getSpannable() {
        String line = Constants.NOTIFICATION_MESSAGE;
        String day = Utils.getDayOfWeek();
        int startPos = line.indexOf(Constants.FIRST_BLOCK);
        String formatted = String.format(line, day);
        Spannable spannable = new SpannableString(formatted);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), startPos, startPos + day.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.DKGRAY), startPos, startPos + day.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
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
