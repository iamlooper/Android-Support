package com.looper.android.support.example

import com.google.android.material.color.DynamicColors
import com.looper.android.support.App

class MyApp : App() {
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic colors
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}