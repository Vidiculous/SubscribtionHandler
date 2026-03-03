package com.subhandler.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.subhandler.model.Subscription
import com.subhandler.utils.parseDate
import java.time.LocalDate
import java.time.ZoneId

const val CHANNEL_ID = "renewal_reminders"
const val EXTRA_SUBSCRIPTION_ID = "subscription_id"

object NotificationScheduler {

    fun scheduleAlarm(context: Context, subscription: Subscription, hour: Int = 9) {
        if (subscription.reminderDays <= 0) return
        val triggerDate = parseDate(subscription.nextRenewalDate)
            .minusDays(subscription.reminderDays.toLong())
        if (!triggerDate.isAfter(LocalDate.now().minusDays(1))) return  // skip past dates

        val triggerMillis = triggerDate
            .atTime(hour.coerceIn(0, 23), 0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = buildPendingIntent(context, subscription.id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            // Fallback to inexact alarm if exact permission not granted
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
        }
    }

    fun cancelAlarm(context: Context, subscriptionId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(buildPendingIntent(context, subscriptionId))
    }

    fun rescheduleAll(context: Context, subscriptions: List<Subscription>, hour: Int = 9) {
        subscriptions.forEach { scheduleAlarm(context, it, hour) }
    }

    private fun buildPendingIntent(context: Context, subscriptionId: Long): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(EXTRA_SUBSCRIPTION_ID, subscriptionId)
        }
        return PendingIntent.getBroadcast(
            context,
            subscriptionId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
