package com.subhandler.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.subhandler.R
import com.subhandler.SubHandlerApp
import com.subhandler.utils.buildNotificationBody
import com.subhandler.utils.buildNotificationTitle
import com.subhandler.utils.getDaysUntil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val subscriptionId = intent.getLongExtra(EXTRA_SUBSCRIPTION_ID, -1L)
        if (subscriptionId == -1L) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val app = context.applicationContext as SubHandlerApp
                val subscription = app.repository.getById(subscriptionId) ?: return@launch

                val daysUntil = getDaysUntil(subscription.nextRenewalDate)
                val title = buildNotificationTitle(subscription, daysUntil)
                val body = buildNotificationBody(subscription, daysUntil)

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                ensureChannel(manager, context)

                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()

                manager.notify(subscriptionId.toInt(), notification)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun ensureChannel(manager: NotificationManager, context: Context) {
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        manager.createNotificationChannel(channel)
    }
}
