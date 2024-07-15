package com.looper.android.support

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import com.looper.android.support.util.AppUtils
import com.looper.android.support.util.SystemServiceUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

open class App : Application() {

    private val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: App? = null

        @SuppressLint("StaticFieldLeak")
        var currentActivity: Activity? = null

        @JvmStatic
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        // Register ActivityLifecycleCallbacks to track the current activity.
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                if (currentActivity === activity) {
                    currentActivity = null
                }
            }

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                if (currentActivity === activity) {
                    currentActivity = null
                }
            }
        })

        // Set the default uncaught exception handler to handle app crashes.
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException(thread, throwable)
        }
    }

    @SuppressLint("NewApi")
    private fun handleUncaughtException(thread: Thread, throwable: Throwable) {
        // Get the stack trace as a string.
        var stackTrace = Log.getStackTraceString(throwable)
        val additionalInfo = """
            Date and Time: ${
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
            Manufacturer: ${Build.MANUFACTURER}
            Model: ${Build.MODEL}
            SDK Version: ${Build.VERSION.SDK_INT}
            App Version Name: ${AppUtils.getVersionName(this)}
            App Version Code: ${AppUtils.getVersionCode(this)}
            Device: ${Build.DEVICE}
            Product: ${Build.PRODUCT}
            Brand: ${Build.BRAND}
            Hardware: ${Build.HARDWARE}
            Thread: ${thread.name}
            Activity: ${currentActivity?.javaClass?.simpleName ?: "Unknown"}
            Exception: ${throwable.message}
            Stack Trace:
            """.trimIndent()
        stackTrace = "$additionalInfo\n$stackTrace"

        // Copy exception to clipboard.
        SystemServiceUtils.copyToClipboard(this, stackTrace)

        // Allow default exception handler to handle logging.
        defaultUncaughtExceptionHandler?.uncaughtException(thread, throwable)

        // Exit the app.
        Process.killProcess(Process.myPid())
        exitProcess(1)
    }
}