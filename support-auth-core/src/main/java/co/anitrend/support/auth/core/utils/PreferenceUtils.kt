package co.anitrend.support.auth.core.utils

import android.content.Context
import android.preference.PreferenceManager

object PreferenceUtils {

    fun getString(context: Context?, key: String): String? {
        return PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
                .getString(key, "")
    }

    fun saveBoolean(context: Context?, key: String, value: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
                .edit()
                .putBoolean(key, value)
                .apply()
    }

    fun getBoolean(context: Context?, key: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
                .getBoolean(key, false)
    }

    fun contains(context: Context?, key: String) {
        PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
                .contains(key)
    }

    fun clear(context: Context?) {
        PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
                .edit().clear().apply()
    }
}