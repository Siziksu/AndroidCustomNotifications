package com.siziksu.notifications.common

import android.util.Log

class Logs {

    companion object {

        private const val TAG = "APP_LOG"

        fun print(message: String) = Log.d(TAG, message)

        fun error(message: String) = Log.e(TAG, message)

        fun error(throwable: Throwable) = Log.e(TAG, throwable.message, throwable)
    }
}
