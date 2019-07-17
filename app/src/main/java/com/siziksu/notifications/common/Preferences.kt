package com.siziksu.notifications.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Preferences(private val context: Context) {

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    fun setString(key: String, value: String) = preferences.edit().putString(key, value).apply()

    fun getString(key: String, defaultValue: String): String? = preferences.getString(key, defaultValue)

    fun deleteKey(key: String) =
        if (preferences.contains(key)) {
            preferences.edit().remove(key).apply()
        } else Unit
}