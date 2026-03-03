package com.subhandler.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.subhandler.model.AppSettings
import com.subhandler.model.BillingCycle
import com.subhandler.model.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val DEFAULT_CURRENCY     = stringPreferencesKey("default_currency")
        val DEFAULT_REMINDER     = intPreferencesKey("default_reminder_days")
        val DEFAULT_CYCLE        = stringPreferencesKey("default_billing_cycle")
        val NOTIFICATION_HOUR    = intPreferencesKey("notification_hour")
        val UPCOMING_WINDOW      = intPreferencesKey("upcoming_window_days")
        val SORT_ORDER           = stringPreferencesKey("sort_order")
    }

    val settings: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            defaultCurrency   = prefs[Keys.DEFAULT_CURRENCY]  ?: "USD",
            defaultReminderDays = prefs[Keys.DEFAULT_REMINDER] ?: 7,
            defaultBillingCycle = BillingCycle.fromString(prefs[Keys.DEFAULT_CYCLE] ?: "MONTHLY"),
            notificationHour  = prefs[Keys.NOTIFICATION_HOUR] ?: 9,
            upcomingWindowDays = prefs[Keys.UPCOMING_WINDOW]  ?: 7,
            sortOrder         = SortOrder.entries.firstOrNull { it.name == prefs[Keys.SORT_ORDER] }
                                    ?: SortOrder.DATE
        )
    }

    suspend fun update(s: AppSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DEFAULT_CURRENCY]  = s.defaultCurrency
            prefs[Keys.DEFAULT_REMINDER]  = s.defaultReminderDays
            prefs[Keys.DEFAULT_CYCLE]     = s.defaultBillingCycle.name
            prefs[Keys.NOTIFICATION_HOUR] = s.notificationHour
            prefs[Keys.UPCOMING_WINDOW]   = s.upcomingWindowDays
            prefs[Keys.SORT_ORDER]        = s.sortOrder.name
        }
    }
}
