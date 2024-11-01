package com.looper.android.support.provider

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * A safe application context provider that allows accessing application context from anywhere in the app.
 * This implementation follows the initialization-on-demand holder idiom for thread-safe singleton pattern.
 */
object AppContextProvider {
    private lateinit var applicationContext: Context

    /**
     * Initializes the AppContextProvider with the application context.
     * This should be called only once in the Application class.
     *
     * @param context The application context
     * @throws IllegalStateException if initialized more than once
     * @throws IllegalArgumentException if non-application context is provided
     */
    @SuppressLint("StaticFieldLeak")
    @JvmStatic
    fun init(context: Context) {
        if (this::applicationContext.isInitialized) {
            throw IllegalStateException("AppContextProvider is already initialized!")
        }

        val appContext = context.applicationContext
        if (appContext !is Application) {
            throw IllegalArgumentException("Expected Application context but received ${context.javaClass.simpleName}")
        }

        applicationContext = appContext
    }

    /**
     * Provides the application context.
     *
     * @return Application context
     * @throws IllegalStateException if accessed before initialization
     */
    @JvmStatic
    fun getContext(): Context {
        if (!this::applicationContext.isInitialized) {
            throw IllegalStateException(
                "AppContextProvider is not initialized. Call AppContextProvider.init(context) in your Application class"
            )
        }
        return applicationContext
    }
}