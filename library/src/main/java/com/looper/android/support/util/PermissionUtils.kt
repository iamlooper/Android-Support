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
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Registers a permission launcher and returns a request function that can be used to request permission later.
     *
     * @param activity the activity to register the launcher.
     * @param context the context of the application.
     * @param permission the permission to request.
     * @param onPermissionGranted optional callback when the permission is granted.
     * @param onPermissionDenied optional callback when the permission is denied.
     *
     * @return a function that can be called to request the permission.
     */
    fun requestPermission(
        activity: AppCompatActivity,
        context: Context,
        permission: String,
        onPermissionGranted: (() -> Unit)? = null,
        onPermissionDenied: (() -> Unit)? = null
    ): () -> Unit {
        var launcher: ActivityResultLauncher<String>? = null

        try {
            launcher =
                activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return {
            if (isPermissionGranted(context, permission)) {
                onPermissionGranted?.invoke()
            } else {
                launcher?.launch(permission)
            }
        }
    }
}