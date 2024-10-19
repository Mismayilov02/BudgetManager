package com.mismayilov.manager

import android.app.Activity
import android.content.Context
import java.util.Locale

object LanguageManager {
    fun getLanguagePosition(language: String?): Int {
        return when (language) {
            "en" -> 0
            "az" -> 1
            "tr" -> 2
            "ru" -> 3
            "de" -> 4
            "fr" -> 5
            else -> 0
        }
    }

    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

}