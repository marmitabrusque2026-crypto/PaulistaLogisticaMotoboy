package com.paulistalogistica.motoboy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PaulistaMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: "🚨 NOVA ENTREGA — PAULISTA"
        val body = message.notification?.body ?: "Nova entrega disponível"

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "paulista_entrega"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val attrs = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(
                channelId,
                "Entregas Paulista",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Alertas de novas entregas"
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(0, 700, 250, 700, 250, 1200)
            channel.setSound(sound, attrs)
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("nova_entrega", true)
        }

        val pending = PendingIntent.getActivity(
            this, 10, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setAutoCancel(true)
            .setOngoing(false)
            .setVibrate(longArrayOf(0, 700, 250, 700, 250, 1200))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setFullScreenIntent(pending, true)
            .setContentIntent(pending)
            .build()

        manager.notify(777, notification)
    }
}
