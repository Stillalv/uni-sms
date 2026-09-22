package com.unisms.app.data.local

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurePreferencesManager(context: Context) {

    private val prefs: SharedPreferences = createPreferences(context)

    private fun createPreferences(context: Context): SharedPreferences {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val masterKey = MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

                return EncryptedSharedPreferences.create(
                    context,
                    "uni_sms_secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (_: Throwable) {
                // Fallback to private preferences if Keystore is corrupted or unsupported on device
            }
        }
        return context.getSharedPreferences("uni_sms_prefs", Context.MODE_PRIVATE)
    }

    companion object {
        private const val KEY_API_KEY = "smsbower_api_key"
        private const val KEY_NOTIF_ENABLED = "setting_notifications_enabled"
        private const val KEY_HAPTIC_ENABLED = "setting_haptic_enabled"
    }

    fun getApiKey(): String? {
        val key = prefs.getString(KEY_API_KEY, null)?.trim()
        return if (key.isNullOrEmpty()) null else key
    }

    fun saveApiKey(apiKey: String) {
        prefs.edit().putString(KEY_API_KEY, apiKey.trim()).apply()
    }

    fun clearApiKey() {
        prefs.edit().remove(KEY_API_KEY).apply()
    }

    fun hasApiKey(): Boolean = !getApiKey().isNullOrEmpty()

    fun isNotificationEnabled(): Boolean = prefs.getBoolean(KEY_NOTIF_ENABLED, true)

    fun setNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIF_ENABLED, enabled).apply()
    }

    fun isHapticEnabled(): Boolean = prefs.getBoolean(KEY_HAPTIC_ENABLED, true)

    fun setHapticEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTIC_ENABLED, enabled).apply()
    }
}
