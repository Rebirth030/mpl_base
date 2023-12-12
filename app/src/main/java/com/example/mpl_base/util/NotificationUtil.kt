package com.example.mpl_base.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mpl_base.R
import com.example.mpl_base.activities.MainActivity

class NotificationUtil {
    companion object {
        private var notificationId: Int = 1

        fun createNotificationChannel(context: Context) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)

            val channel = NotificationChannel(
                context.getString(R.string.channel_id),
                name,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = descriptionText
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }

        fun sendNotification(
            context: Context,
            title: String,
            text: String,
            icon: Int,
            intent: Intent
        ) {
            val pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder =
                NotificationCompat.Builder(context, context.getString(R.string.channel_id))
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(NotificationCompat.DEFAULT_ALL or NotificationCompat.DEFAULT_SOUND)
                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                cancelAll()
                notify(notificationId, builder.build())
                notificationId++
            }
        }
    }
}