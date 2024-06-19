package com.looper.android.support

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import com.looper.android.support.util.SystemServiceUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

open class App : Application() {

    private val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun onCreate() {
        super.onCreate()

        // Set the default uncaught exception handler to handle app crashes.
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException(thread, throwable)
        }
    }

    @SuppressLint("NewApi")
    private fun handleUncaughtException(thread: Thread, throwable: Throwable) {
        // Get the stack trace as a string.
        val stackTrace = Log.getStackTraceString(throwable)

        // Copy exception to clipboard.
        SystemServiceUtils.copyToClipboard(this, stackTrace)

        // Notify user via toast.
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(
                applicationContext,
                getString(R.string.app_crashed_message),
                Toast.LENGTH_LONG
            ).show()
        }

        // Allow default exception handler to handle logging.
        defaultUncaughtExceptionHandler?.uncaughtException(thread, throwable)

        // Gracefully exit the app.
        exitProcess(1)
    }
}