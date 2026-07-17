package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import io.sentry.android.core.SentryAndroid

class HOSApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 1. Initialize Sentry Android for automatic exception and crash tracking
        SentryAndroid.init(this) { options ->
            // A valid-structured placeholder DSN for Sentry
            options.dsn = "https://e0762cf3ea85474ca8f57f4955bca9e9@o450505.ingest.sentry.io/4505051"
            options.isEnableUncaughtExceptionHandler = true
            options.isAnrEnabled = true
            options.environment = "production"
        }
        
        Log.d("HOSApplication", "Sentry SDK initialized successfully.")

        // 2. Safely initialize FirebaseApp manually if not already done automatically
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
                Log.d("HOSApplication", "Firebase initialized manually successfully.")
            } else {
                Log.d("HOSApplication", "Firebase already initialized automatically.")
            }
        } catch (e: Exception) {
            Log.e("HOSApplication", "Manual Firebase initialization failed", e)
        }
        
        // 3. Create push notification channels for Android O+
        createNotificationChannels()
        
        // 4. Log FCM Token on startup
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("HOSApplication", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("HOSApplication", "Current FCM registration token: $token")
            }
        } catch (e: Exception) {
            Log.e("HOSApplication", "Firebase not initialized fully yet, FCM token retrieve skipped.", e)
        }
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "amone_hos_fcm_channel"
            val channelName = "AMONE HOS Powiadomienia"
            val channelDescription = "Kanał dla powiadomień systemowych, aktualizacji i wsparcia somatycznego"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
