package com.looper.android.support.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)    
    
        // Configure edge-to-edge display.
        configureEdgeToEdgeDisplay()
    }

    // Configures the edge-to-edge display for the activity.
    private fun configureEdgeToEdgeDisplay() {
        // Get the window object associated with this activity.
        val window: Window = window

        // Enable edge-to-edge display by setting decor fits system windows to false.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Apply content overlap fix for landscape orientation with mobile_navigation.xml buttons.
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val rootView: ViewGroup = window.decorView.findViewById(android.R.id.content)

            // Apply window insets listener to adjust padding and margins.
            ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
                // Get the insets for system bars.
                val insets: Insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                // Adjust padding and margins for the root view.
                val params: ViewGroup.MarginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
                view.setPadding(
                    params.leftMargin + insets.left,
                    0,
                    params.rightMargin + insets.right,
                    params.bottomMargin + insets.bottom
                )
                params.topMargin = 0
                view.layoutParams = params

                // Return the adjusted window insets.
                windowInsets
            }
        }
    }
    
    protected open fun getContentView(): Int? {
        return null
    }
}