package com.looper.android.support.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPreferencesUtils(context: Context) {

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Stores a key-value pair in SharedPreferences.
     *
     * @param key The key under which the value should be stored.
     * @param value The value to be stored.
     * @throws IllegalArgumentException if the value type is unsupported.
     */
    @Suppress("UNCHECKED_CAST")
    fun put(key: String, value: Any) {
        val editor = sharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            is Set<*> -> editor.putStringSet(key, value as Set<String>)
            else -> throw IllegalArgumentException("Unsupported value type")
        }
        editor.apply()
    }

    /**
     * Retrieves a value from SharedPreferences.
     *
     * @param key The key whose value should be retrieved.
     * @param defaultValue The default value to return if the key does not exist.
     * @return The value associated with the key, or the default value if the key does not exist.
     * @throws IllegalArgumentException if the default value type is unsupported.
     */
    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    fun <T> get(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue)
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            is Set<*> -> sharedPreferences.getStringSet(key, defaultValue as Set<String>)
            else -> throw IllegalArgumentException("Unsupported default value type")
        } as T
    }

    /**
     * Removes a key-value pair from SharedPreferences.
     *
     * @param key The key to be removed.
     */
    fun remove(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    // Clears all key-value pairs in SharedPreferences.
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    // Returns the SharedPreferences instance.
    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}
