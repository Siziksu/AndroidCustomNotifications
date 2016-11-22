package com.siziksu.notifications.ui.activity;

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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.RemoteViews;

import com.siziksu.notifications.R;
import com.siziksu.notifications.common.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.buttonNormal)
    public void onLeftButtonClick() {
        showNotification();
    }

    @OnClick(R.id.buttonCustom)
    public void onRightButtonClick() {
        showCustomNotification();
    }

    private void showNotification() {
        TaskStackBuilder stack = TaskStackBuilder.create(this);
        stack.addParentStack(MainActivity.class);
        stack.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent pending = stack.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.notification_icon);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle().bigText(getSpannable());
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setLargeIcon(icon)
                .setContentTitle("Jane Doe")
                .setContentText(getSpannable())
                .setStyle(style)
                .setSound(sound)
                .setColor(Color.GREEN)
                .setContentIntent(pending)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = builder.build();
        notificationManager.notify(0, notification);
    }

    private void showCustomNotification() {
        RemoteViews remoteViews = createRemoteViews(
                this,
                R.layout.notification,
                R.drawable.ic_phone_call_24dp,
                "Jane Doe",
                getSpannable(),
                0
        );
        RemoteViews bigRemoteViews = createRemoteViews(
                this,
                R.layout.notification_big,
                R.drawable.ic_phone_call_48dp,
                "Jane Doe",
                getSpannable(),
                R.drawable.ic_exclamation_48dp
        );
        TaskStackBuilder stack = TaskStackBuilder.create(this);
        stack.addParentStack(MainActivity.class);
        stack.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent pending = stack.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_phone_call_24dp)
                .setSound(sound)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigRemoteViews)
                .setContentIntent(pending)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    @NonNull
    private Spannable getSpannable() {
        String line = "Today is %1$s, and I wanted to say good morning! This line will exceed a lot the normal height of a notification.";
        String day = "Sunday";
        int startPos = line.indexOf("%1$s");
        String formatted = String.format(line, day);
        Spannable spannable = new SpannableString(formatted);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), startPos, startPos + day.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.DKGRAY), startPos, startPos + day.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private RemoteViews createRemoteViews(Context context, int layout, int iconResource, String title, CharSequence text, int endIconResource) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);
        remoteViews.setImageViewResource(R.id.notificationIcon, iconResource);
        remoteViews.setTextViewText(R.id.notificationTitle, title);
        remoteViews.setTextViewText(R.id.notificationText, text);
        remoteViews.setTextViewText(R.id.notificationTime, getTime());
        if (endIconResource != 0) {
            remoteViews.setImageViewResource(R.id.notificationEndIcon, endIconResource);
        }
        return remoteViews;
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault());
        return format.format(Calendar.getInstance().getTime());
    }
}
