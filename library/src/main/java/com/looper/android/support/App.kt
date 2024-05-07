package com.looper.android.support

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.looper.android.support.util.SystemServiceUtils
import com.looper.android.support.util.UIUtils
import com.looper.android.support.util.CoroutineUtils
import kotlin.system.exitProcess

open class App : Application() {

    private lateinit var coroutineUtils: CoroutineUtils
    private val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun onCreate() {
        super.onCreate()

        // Initialize.
        coroutineUtils = CoroutineUtils()

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
        coroutineUtils.main("handleUncaughtExceptionToast") {
            UIUtils.showToast(
                applicationContext,
                getString(R.string.app_crashed_message),
                UIUtils.TOAST_LENGTH_LONG
            )
        }

        // Allow default exception handler to handle logging.
        defaultUncaughtExceptionHandler?.uncaughtException(thread, throwable)

        // Gracefully exit the app.
        exitProcess(1)
    }
}