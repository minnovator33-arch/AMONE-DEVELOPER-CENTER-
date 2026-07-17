package com.example.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.sentry.Sentry

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_SERVICE", "Refreshed token: $token")
        // In a production app, send this token to your backend server.
        Sentry.addBreadcrumb("FCM token refreshed: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM_SERVICE", "From: ${remoteMessage.from}")

        // Check if message contains data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM_SERVICE", "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains notification payload
        remoteMessage.notification?.let {
            Log.d("FCM_SERVICE", "Message Notification Body: ${it.body}")
            sendNotification(it.title ?: "AMONE HOS", it.body ?: "")
        } ?: run {
            // Handle data-only push notifications
            val title = remoteMessage.data["title"] ?: "AMONE HOS"
            val body = remoteMessage.data["body"] ?: "Nowe powiadomienie somatyczne"
            sendNotification(title, body)
        }
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "amone_hos_fcm_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Standard fallback, can customize later
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since Android Oreo, notification channels are required.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "AMONE HOS Powiadomienia",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Kanał dla powiadomień systemowych, aktualizacji i wsparcia somatycznego"
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
