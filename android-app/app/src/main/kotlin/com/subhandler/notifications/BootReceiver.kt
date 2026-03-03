package com.subhandler.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.subhandler.SubHandlerApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val app = context.applicationContext as SubHandlerApp
                val subscriptions = app.repository.getAllOnce()
                NotificationScheduler.rescheduleAll(context, subscriptions)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
