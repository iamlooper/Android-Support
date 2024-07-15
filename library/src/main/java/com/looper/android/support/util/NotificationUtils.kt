package com.looper.android.support.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtils {

    /**
     * Builds a notification channel if it doesn't already exist, with compatibility for different Android versions.
     *
     * @param context, the context of the application.
     * @param notificationChannelId, the unique ID for the notification channel.
     * @param notificationChannelName, the user-visible name of the notification channel.
     */
    fun buildChannel(
        context: Context,
        notificationChannelId: String,
        notificationChannelName: String
    ) {
        // Check if the Android version is Oreo or higher, as NotificationChannel is not available in lower versions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Check if the notification channel already exists.
            val existingChannel = notificationManager.getNotificationChannel(notificationChannelId)

            if (existingChannel == null) {
                // Create a new notification channel.
                val notificationChannel = NotificationChannel(
                    notificationChannelId,
                    notificationChannelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                // Create the notification channel.
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }

    /**
     * Cancels the notification with the specified ID.
     *
     * @param context, the context of the application.
     * @param notificationId, the ID of the notification.
     */
    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId)
    }

    /**
     * Builds and returns a basic notification builder.
     *
     * @param context, the context of the application.
     * @param channelId, the ID of the notification channel.
     * @param smallIconResourceId, the resource ID of the small icon.
     * @return the notification builder.
     */
    fun createNotificationBuilder(
        context: Context,
        channelId: String,
        smallIconResourceId: Int
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIconResourceId)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    /**
     * Displays a notification using the provided builder.
     *
     * @param context, the context of the application.
     * @param notificationId, the ID of the notification.
     * @param notificationBuilder, the notification builder.
     */
    @SuppressLint("MissingPermission")
    fun displayNotification(
        context: Context,
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder
    ) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}