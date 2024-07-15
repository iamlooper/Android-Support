package com.looper.android.support.util

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
     * Registers a permission launcher.
     *
     * @param activity the activity to register the launcher.
     * @param context the context of the application.
     * @param onPermissionGranted optional callback when the permission is granted.
     * @param onPermissionDenied optional callback when the permission is denied.
     *
     * @return the registered ActivityResultLauncher.
     */
    fun registerPermissionLauncher(
        activity: AppCompatActivity,
        context: Context,
        onPermissionGranted: (() -> Unit)? = null,
        onPermissionDenied: (() -> Unit)? = null
    ): ActivityResultLauncher<String> {
        return activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
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
    }

    /**
     * Requests the specified permission from the user.
     *
     * @param launcher the ActivityResultLauncher to launch the permission request.
     * @param context the context of the application.
     * @param permission the permission to request.
     */
    fun requestPermission(
        launcher: ActivityResultLauncher<String>,
        context: Context,
        permission: String
    ) {
        if (isPermissionGranted(context, permission)) {
            return
        }

        launcher.launch(permission)
    }
}