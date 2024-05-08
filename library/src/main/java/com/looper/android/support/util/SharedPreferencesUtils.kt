package com.looper.android.support.util

import android.content.Context
import android.content.SharedPreferences

import androidx.preference.PreferenceManager

class SharedPreferencesUtils private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private var instance: SharedPreferencesUtils? = null

        fun getInstance(context: Context): SharedPreferencesUtils {
            if (instance == null) {
                instance = SharedPreferencesUtils(context.applicationContext)
            }
            return instance!!
        }
    }

    // Save a string value to shared preferences.
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Get a string value from shared preferences.
    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue)!!
    }
    
    // Save a set of strings to shared preferences.
    fun saveStringSet(key: String, value: Set<String>) {
        sharedPreferences.edit().putStringSet(key, value).apply()
    }

    // Get a set of strings from shared preferences.
    fun getStringSet(key: String, defaultValue: Set<String>): Set<String> {
        return sharedPreferences.getStringSet(key, defaultValue)!!
    }    

    // Save an integer value to shared preferences.
    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    // Get an integer value from shared preferences.
    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // Save a boolean value to shared preferences.
    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    // Get a boolean value from shared preferences.
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Remove a value from shared preferences.
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    // Clear all values from shared preferences.
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}