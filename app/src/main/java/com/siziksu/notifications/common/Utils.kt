package com.siziksu.notifications.common

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Utils {

    val time: String
        get() {
            val format = SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault())
            return format.format(Calendar.getInstance().time)
        }

    val spannable: Spannable
        get() {
            val line = Constants.FAKE_MESSAGE
            val day = dayOfWeek
            val startPos = line.indexOf(Constants.FIRST_BLOCK)
            val formatted = String.format(line, day)
            val spannable = SpannableString(formatted)
            spannable.setSpan(StyleSpan(Typeface.BOLD), startPos, startPos + day.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(ForegroundColorSpan(Color.DKGRAY), startPos, startPos + day.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannable
        }

    private val dayOfWeek: String
        get() {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_WEEK)
            return when (day) {
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                Calendar.SUNDAY -> "Sunday"
                else -> ""
            }
        }

    fun getInboxSpannable(owner: String, message: String): Spannable {
        val line = Constants.INBOX_MESSAGE
        val startPos = line.indexOf(Constants.FIRST_BLOCK)
        val formatted = String.format(line, owner, message)
        val spannable = SpannableString(formatted)
        spannable.setSpan(StyleSpan(Typeface.BOLD), startPos, startPos + owner.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(Color.DKGRAY), startPos, startPos + owner.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }
}
