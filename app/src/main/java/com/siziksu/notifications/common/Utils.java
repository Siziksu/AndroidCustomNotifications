package com.siziksu.notifications.common;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault());
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
            default:
                return "";
        }
    }

    @NonNull
    public static Spannable getSpannable() {
        String line = Constants.FAKE_MESSAGE;
        String day = getDayOfWeek();
        int startPos = line.indexOf(Constants.FIRST_BLOCK);
        String formatted = String.format(line, day);
        Spannable spannable = new SpannableString(formatted);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), startPos, startPos + day.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.DKGRAY), startPos, startPos + day.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    @NonNull
    public static Spannable getInboxSpannable(String owner, String message) {
        String line = Constants.INBOX_MESSAGE;
        int startPos = line.indexOf(Constants.FIRST_BLOCK);
        String formatted = String.format(line, owner, message);
        Spannable spannable = new SpannableString(formatted);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), startPos, startPos + owner.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.DKGRAY), startPos, startPos + owner.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}
