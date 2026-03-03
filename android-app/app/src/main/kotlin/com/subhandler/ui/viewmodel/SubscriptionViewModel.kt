package com.subhandler.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.subhandler.SubHandlerApp
import com.subhandler.data.SettingsRepository
import com.subhandler.data.SubscriptionRepository
import com.subhandler.model.AppSettings
import com.subhandler.model.SortOrder
import com.subhandler.model.SpendSummary
import com.subhandler.model.Subscription
import com.subhandler.notifications.NotificationScheduler
import com.subhandler.utils.advanceByOneCycle
import com.subhandler.utils.computeSpendSummary
import com.subhandler.utils.getDaysUntil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val subscriptions: List<Subscription> = emptyList(),
    val isLoading: Boolean = true
)

class SubscriptionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: SubscriptionRepository =
        (application as SubHandlerApp).repository
    private val settingsRepo: SettingsRepository =
        (application as SubHandlerApp).settingsRepository
    private val context: Context get() = getApplication<SubHandlerApp>()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val settings: StateFlow<AppSettings> = settingsRepo.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppSettings())

    val spendSummary: StateFlow<SpendSummary> = _uiState
        .map { computeSpendSummary(it.subscriptions) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SpendSummary())

    val upcomingRenewals: StateFlow<List<Subscription>> = combine(
        _uiState, settings
    ) { state, s ->
        state.subscriptions
            .filter { getDaysUntil(it.nextRenewalDate) in 0..s.upcomingWindowDays.toLong() }
            .sortedBy { getDaysUntil(it.nextRenewalDate) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            combine(repo.subscriptions, settings) { list, s ->
                val sorted = when (s.sortOrder) {
                    SortOrder.DATE -> list.sortedBy { it.nextRenewalDate }
                    SortOrder.NAME -> list.sortedBy { it.name.lowercase() }
                    SortOrder.COST -> list.sortedByDescending { it.cost }
                }
                UiState(subscriptions = sorted, isLoading = false)
            }.collect { _uiState.value = it }
        }
    }

    fun addSubscription(subscription: Subscription) {
        viewModelScope.launch {
            val newId = repo.insert(subscription)
            val saved = subscription.copy(id = newId, notificationAlarmId = newId.toInt())
            repo.update(saved)
            NotificationScheduler.scheduleAlarm(context, saved, settings.value.notificationHour)
        }
    }

    fun updateSubscription(subscription: Subscription) {
        viewModelScope.launch {
            NotificationScheduler.cancelAlarm(context, subscription.id)
            val updated = subscription.copy(
                updatedAt = System.currentTimeMillis(),
                notificationAlarmId = subscription.id.toInt()
            )
            repo.update(updated)
            NotificationScheduler.scheduleAlarm(context, updated, settings.value.notificationHour)
        }
    }

    fun deleteSubscription(id: Long) {
        viewModelScope.launch {
            NotificationScheduler.cancelAlarm(context, id)
            repo.delete(id)
        }
    }

    fun renewSubscription(subscription: Subscription) {
        viewModelScope.launch {
            NotificationScheduler.cancelAlarm(context, subscription.id)
            val newDate = advanceByOneCycle(subscription)
            val updated = subscription.copy(
                nextRenewalDate = newDate,
                updatedAt = System.currentTimeMillis()
            )
            repo.update(updated)
            NotificationScheduler.scheduleAlarm(context, updated, settings.value.notificationHour)
        }
    }

    fun updateSettings(newSettings: AppSettings) {
        val oldHour = settings.value.notificationHour
        viewModelScope.launch {
            settingsRepo.update(newSettings)
            // Reschedule all alarms if notification hour changed
            if (newSettings.notificationHour != oldHour) {
                val subs = repo.getAllOnce()
                NotificationScheduler.rescheduleAll(context, subs, newSettings.notificationHour)
            }
        }
    }
}
