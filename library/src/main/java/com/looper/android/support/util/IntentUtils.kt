package com.looper.android.support.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.looper.android.support.R
import java.io.File

object IntentUtils {

    /**
     * Opens a provided URL using Intent.
     *
     * @param context, the context which is needed to start intent activity.
     * @param url, the string which has the URL to be shown.
     */
    fun openURL(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Opens an app in Play Store.
     *
     * @param context, the context which is needed to start intent activity.
     * @param packageName, the string which has the package name.
     */
    fun openAppInPlayStore(context: Context, packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=$packageName")
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intentWeb = Intent(Intent.ACTION_VIEW)
            intentWeb.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            context.startActivity(intentWeb)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Shares a file using an Intent.
     *
     * @param context, the context from which the method is called.
     * @param file, the File object representing the file to be shared.
     * @param mimeType, the MIME type of the file (e.g., "image/jpeg", "text/plain").
     */
    fun shareFile(context: Context, file: File, mimeType: String) {
        // Get a content URI for the file using FileProvider.
        val fileUri: Uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )

        // Create an Intent with action ACTION_SEND to share the file.
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = mimeType
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission on the URI.

        // Create a chooser Intent to display app options for sharing.
        val chooserIntent = Intent.createChooser(shareIntent, context.getString(R.string.share_file))

        // Check if there is an app available to handle the share action.
        if (shareIntent.resolveActivity(context.packageManager) != null) {
            // Start the chosen app to share the file.
            context.startActivity(chooserIntent)
        } else {
            // Handle the case where no appropriate app is installed to handle the share action.
            UIUtils.showToast(context, context.getString(R.string.no_app_found_to_share_file), UIUtils.TOAST_LENGTH_SHORT)
        }
    }    
}