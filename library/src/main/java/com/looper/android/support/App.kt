package com.looper.android.support

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.looper.android.support.provider.AppContextProvider
import com.looper.android.support.util.AppUtil
import com.looper.android.support.util.SystemServiceUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Stack

open class App : Application() {

    private val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var currentActivity: Activity? = null

        // Stack to maintain activity history
        private val activityStack = Stack<Activity>()

        // Getter for current activity
        fun getCurrentActivity(): Activity? = currentActivity

        // Getter for top activity from stack
        fun getTopActivity(): Activity? =
            if (!activityStack.isEmpty()) activityStack.peek() else null
    }

    override fun onCreate() {
        super.onCreate()

        AppContextProvider.init(this)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityStack.push(activity)
                currentActivity = activity  // Update current activity when created
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity

                // Validate stack integrity
                if (!activityStack.contains(activity)) {
                    activityStack.push(activity)
                }
            }

            override fun onActivityPaused(activity: Activity) {
                // Only clear if it's the current activity being paused
                if (currentActivity === activity) {
                    // Instead of setting to null, set to the previous activity in stack if available
                    currentActivity = if (activityStack.size > 1) {
                        val index = activityStack.indexOf(activity)
                        if (index > 0) activityStack[index - 1] else null
                    } else null
                }
            }

            override fun onActivityStopped(activity: Activity) {
                // Similar logic to onActivityPaused
                if (currentActivity === activity) {
                    currentActivity = if (activityStack.size > 1) {
                        val index = activityStack.indexOf(activity)
                        if (index > 0) activityStack[index - 1] else null
                    } else null
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                activityStack.remove(activity)

                if (currentActivity === activity) {
                    // Set current activity to the top of stack if available
                    currentActivity = getTopActivity()
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                // No tracking needed for save instance state
            }
        })

        // Set the uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException(thread, throwable)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun handleUncaughtException(thread: Thread, throwable: Throwable) {
        // Get current activity from both sources for accuracy
        val currentActivityName = getCurrentActivity()?.javaClass?.simpleName
        val topActivityName = getTopActivity()?.javaClass?.simpleName

        val stackTrace = buildString {
            append(
                """
                Date and Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}
                App Name: ${AppUtil.getAppName(this@App)}
                App Version: ${AppUtil.getAppVersionName(this@App)} (${
                    AppUtil.getAppVersionCode(
                        this@App
                    )
                })
                Device: ${Build.MODEL} (SDK ${Build.VERSION.SDK_INT})
                Current Activity: ${currentActivityName ?: topActivityName ?: "Unknown"}
                Activity Stack: ${activityStack.joinToString(" -> ") { it.javaClass.simpleName }}
                Exception: ${throwable.message}
                Stack Trace:
            """.trimIndent()
            )
            append("\n")
            append(Log.getStackTraceString(throwable))
        }

        // Copy exception to clipboard
        SystemServiceUtil.copyToClipboard(this, stackTrace)

        // Notify user via toast
        try {
            Toast.makeText(
                this,
                getString(R.string.app_crashed_message),
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Allow default exception handler now
        defaultUncaughtExceptionHandler?.uncaughtException(thread, throwable)
    }
}