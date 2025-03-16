package com.example.sharedpreferences.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PreferencesManager(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var userName: String
        get() = sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
        set(value) = sharedPreferences.edit().putString(KEY_USER_NAME, value).apply()

    var isDarkTheme: Boolean
        get() = sharedPreferences.getBoolean(KEY_DARK_THEME, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_DARK_THEME, value).apply()

    var preferredLanguage: String
        get() = sharedPreferences.getString(KEY_PREFERRED_LANGUAGE, "es") ?: "es"
        set(value) = sharedPreferences.edit().putString(KEY_PREFERRED_LANGUAGE, value).apply()

    var notificationVolume: Float
        get() = sharedPreferences.getFloat(KEY_NOTIFICATION_VOLUME, 0.5f)
        set(value) = sharedPreferences.edit().putFloat(KEY_NOTIFICATION_VOLUME, value).apply()

    var lastAccessTime: String
        get() = sharedPreferences.getString(KEY_LAST_ACCESS, "") ?: ""
        set(value) = sharedPreferences.edit().putString(KEY_LAST_ACCESS, value).apply()

    var lastLocation: String
        get() = sharedPreferences.getString(KEY_LAST_LOCATION, "") ?: ""
        set(value) = sharedPreferences.edit().putString(KEY_LAST_LOCATION, value).apply()

    var totalUsageTime: Long
        get() = sharedPreferences.getLong(KEY_TOTAL_USAGE_TIME, 0L)
        set(value) = sharedPreferences.edit().putLong(KEY_TOTAL_USAGE_TIME, value).apply()

    private var lastOpenTime: Long
        get() = sharedPreferences.getLong(KEY_LAST_OPEN_TIME, 0L)
        set(value) = sharedPreferences.edit().putLong(KEY_LAST_OPEN_TIME, value).apply()

    fun updateAccessTime() {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        lastAccessTime = localDateTime.toString()
    }

    fun startSession() {
        lastOpenTime = System.currentTimeMillis()
        updateAccessTime()
    }

    fun endSession() {
        val currentTime = System.currentTimeMillis()
        val sessionDuration = currentTime - lastOpenTime
        totalUsageTime += sessionDuration
    }

    companion object {
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_PREFERRED_LANGUAGE = "preferred_language"
        private const val KEY_NOTIFICATION_VOLUME = "notification_volume"
        private const val KEY_LAST_ACCESS = "last_access"
        private const val KEY_LAST_LOCATION = "last_location"
        private const val KEY_TOTAL_USAGE_TIME = "total_usage_time"
        private const val KEY_LAST_OPEN_TIME = "last_open_time"
    }
}
