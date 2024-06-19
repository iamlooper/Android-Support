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
     * @param context, the context of the application.
     * @param permission, the permission to check.
     *
     * @return `true` if the permission is granted, `false` otherwise.
    */
    fun isGranted(context: Context, permission: String): Boolean {
        // Check if the permission is granted.
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Requests the specified permission from the user.
     *
     * @param activity, the activity to show the permission request dialog.
     * @param context, the context of the application.
     * @param permission, the permission to request.
    */
    fun request(activity: AppCompatActivity, context: Context, permission: String) {
        // Check if the permission is already granted.
        if (isGranted(context, permission)) {
            return
        }

        // Create a launcher for requesting permissions.
        val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _ ->
            // Check if the permission was granted.
            if (!isGranted(context, permission)) {
                // If the permission was not granted, show a toast message to inform the user.
                Toast.makeText(
                    context,
                    context.getString(R.string.permission_not_granted_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Ask for the permission.
        requestPermissionLauncher.launch(permission)
    }
}