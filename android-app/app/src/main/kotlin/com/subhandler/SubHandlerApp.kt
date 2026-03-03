package com.subhandler

import android.app.Application
import com.subhandler.data.AppDatabase
import com.subhandler.data.SettingsRepository
import com.subhandler.data.SubscriptionRepository

class SubHandlerApp : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { SubscriptionRepository(database.subscriptionDao()) }
    val settingsRepository by lazy { SettingsRepository(this) }
}
