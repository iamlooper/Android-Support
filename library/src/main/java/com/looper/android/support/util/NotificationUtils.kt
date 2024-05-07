package com.looper.android.support.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtils {

   /**
     * Builds a notification channel if it doesn't already exist.
     *
     * @param context, the context of the application.
     * @param notificationChannelId, the unique ID for the notification channel.
     * @param notificationChannelName, the user-visible name of the notification channel.
     */
    fun buildChannel(context: Context, notificationChannelId: String, notificationChannelName: String) {
        // Get the notification manager service.
        val notificationManager = NotificationManagerCompat.from(context)

        // Check if the notification channel already exists.
        val existingChannel = notificationManager.getNotificationChannel(notificationChannelId)

        if (existingChannel == null) {
            // Create a new notification channel.
            val notificationChannel = NotificationChannel(notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_DEFAULT)

            // Create the notification channel.
            notificationManager.createNotificationChannel(notificationChannel)
       }
    }

    /**
     * Builds a notification with the specified parameters.
     *
     * @param context, the context of the application.
     * @param channelId, the ID of the notification channel.
     * @param notificationId, the ID of the notification.
     * @param notificationType, the type of the notification.
     * @param notificationTitle, the title of the notification.
     * @param notificationText, the text of the notification.
    */
    @SuppressLint("MissingPermission")
    fun build(context: Context, channelId: String, notificationId: Int, notificationType: String, notificationTitle: String, notificationText: String, smallIconResourceId: Int) {
        // Create a new notification builder.
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIconResourceId)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Set the notification type.
        when (notificationType) {
            "ongoing" -> notificationBuilder.setProgress(0, 0, true)
        }

        // Build the notification.
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    /**
     * Cancels the notification with the specified ID.
     *
     * @param context, the context of the application.
     * @param notificationId, the ID of the notification.
    */
    fun cancel(context: Context, notificationId: Int) {
        // Get the notification manager service.
        val notificationManager = NotificationManagerCompat.from(context)

        // Cancel the notification.
        notificationManager.cancel(notificationId)
    }
}