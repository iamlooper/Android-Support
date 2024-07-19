package com.looper.android.support.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure edge-to-edge display.
        configureEdgeToEdgeDisplay()
    }

    // Configures the edge-to-edge display for the activity.
    private fun configureEdgeToEdgeDisplay() {
        // Enable edge-to-edge display.
        enableEdgeToEdge()

        // Resolve visual overlap in button mode.
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewCompat.setOnApplyWindowInsetsListener(window.decorView.findViewById(android.R.id.content)) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                // Apply the insets as a margin to the view.
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    leftMargin = insets.left
                    bottomMargin = insets.bottom
                    rightMargin = insets.right
                }

                // Propagate window insets to child views.
                windowInsets
            }
        }
    }
}