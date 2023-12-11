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

class NotificationUtil
{
    companion object
    {
        private var notificationId: Int = 1

        fun createNotificationChannel(context: Context)
        {

        }

        fun sendNotification(context: Context, title: String, text: String, icon: Int, intent: Intent)
        {

        }
    }
}