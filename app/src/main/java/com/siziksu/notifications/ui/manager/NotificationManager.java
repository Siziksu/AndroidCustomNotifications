package com.siziksu.notifications.ui.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.siziksu.notifications.R;
import com.siziksu.notifications.broadcast.NotificationListener;
import com.siziksu.notifications.common.constants.Constants;
import com.siziksu.notifications.common.constants.Utils;
import com.siziksu.notifications.ui.activity.MainActivity;

public class NotificationManager {

    private static final int NORMAL_NOTIFICATION_ID = 1147;
    private static final int NORMAL_NOTIFICATION_REQUEST_CODE = 1147;
    private static final int NO_FLAGS = 0;
    private static final int NO_ICON = 0;

    private final Context context;

    public NotificationManager(Context context) {
        this.context = context;
    }

    public void showNotification() {
        TaskStackBuilder stack = TaskStackBuilder.create(context);
        stack.addParentStack(MainActivity.class);
        stack.addNextIntent(new Intent(context, MainActivity.class));
        PendingIntent pending = stack.getPendingIntent(NORMAL_NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_ONE_SHOT);
        Intent cancel = new Intent(NotificationListener.INTENT);
        cancel.putExtra(NotificationListener.EXTRA_ID, NORMAL_NOTIFICATION_ID);
        cancel.putExtra(NotificationListener.EXTRA_ACTION, NotificationListener.ACTION_CANCEL);
        PendingIntent pendingCancel = PendingIntent.getBroadcast(context, NORMAL_NOTIFICATION_REQUEST_CODE, cancel, NO_FLAGS);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle().bigText(getSpannable());
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setLargeIcon(icon)
                .setContentTitle(context.getString(R.string.jane_doe))
                .setContentText(getSpannable())
                .setStyle(style)
                .setSound(sound)
                .setColor(Color.GREEN)
                .addAction(NO_ICON, context.getString(R.string.action_cancel), pendingCancel)
                .setContentIntent(pending)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = builder.build();
        notificationManager.notify(NORMAL_NOTIFICATION_ID, notification);
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
}
