package com.harish.travelappui.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.harish.travelappui.HomeActivity
import com.harish.travelappui.R
import com.harish.travelappui.repository.UserRepository

class TravelAppMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage?.notification?.let {
            Log.e("FCM", "Message Notification Body: ${it.body}")
            startTripNotification(it.title!!,it.body!!)
        }
    }

    override fun onNewToken(token: String) {
        Log.e("FCM","NEW TOKEN $token")
        UserRepository(applicationContext).saveFCMToken(token)

    }

    private fun initChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT < 26) {
            return
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

        notificationManager.createNotificationChannel(channel)
    }
    private fun startTripNotification(title: String, body: String) {

        initChannel("1022", "_CHANNEL_NAME")

        val resultIntent = Intent(this, HomeActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val     notification = NotificationCompat.Builder(applicationContext,"1022")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.art)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.art))
        notification.setContentIntent(resultPendingIntent)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification.build())
    }
}