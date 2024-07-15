package com.looper.android.support.util

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.looper.android.support.R

object PermissionUtils {

    /**
     * Checks if the specified permission is granted.
     *
     * @param context the context of the application.
     * @param permission the permission to check.
     *
     * @return `true` if the permission is granted, `false` otherwise.
     */
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Requests the specified permission from the user with optional callbacks.
     *
     * @param activity the activity to show the permission request dialog.
     * @param context the context of the application.
     * @param permission the permission to request.
     * @param onPermissionGranted optional callback when the permission is granted.
     * @param onPermissionDenied optional callback when the permission is denied.
     */
    fun requestPermission(
        activity: AppCompatActivity,
        context: Context,
        permission: String,
        onPermissionGranted: (() -> Unit)? = null,
        onPermissionDenied: (() -> Unit)? = null
    ) {
        // Check if the permission is already granted.
        if (isPermissionGranted(context, permission)) {
            onPermissionGranted?.invoke()
            return
        }

        // Create a launcher for requesting the permission.
        val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _ ->
            if (isPermissionGranted(context, permission)) {
                onPermissionGranted?.invoke()
            } else {
                onPermissionDenied?.invoke() ?: run {
                    Toast.makeText(
                        context,
                        context.getString(R.string.permission_not_granted_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Request the permission.
        requestPermissionLauncher.launch(permission)
    }
}