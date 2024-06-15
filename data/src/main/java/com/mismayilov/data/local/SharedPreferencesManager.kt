package com.mismayilov.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SharedPreferencesManager {
    private const val PREFERANCE_NAME = "BUDGET_MANAGER"
    private lateinit var preferences: SharedPreferences
    private lateinit var editor:SharedPreferences.Editor

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        preferences = EncryptedSharedPreferences.create(
            context,
            PREFERANCE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        editor = preferences.edit()

    }

    fun removeValue(key: String) {
        editor.remove(key.lowercase())
        editor.apply()
    }

    fun isContained(s: String?): Boolean {
        return preferences.contains(s)
    }

    fun <T> setValue(key: String, value: T) {
        when (value) {
            is String -> editor.putString(key.lowercase(), value)
            is Boolean -> editor.putBoolean(key.lowercase(), value)
            is Int -> editor.putInt(key.lowercase(), value)
            is Long -> editor.putLong(key.lowercase(), value)
        }
        editor.apply()
    }

   fun <T>getValue(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> preferences.getString(key.lowercase(), defaultValue) as T
            is Boolean -> preferences.getBoolean(key.lowercase(), defaultValue) as T
            is Int -> preferences.getInt(key.lowercase(), defaultValue) as T
            is Long -> preferences.getLong(key.lowercase(), defaultValue) as T
            else -> throw IllegalArgumentException("Unknown type")
        }
    }
}