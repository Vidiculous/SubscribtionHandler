package com.subhandler.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.subhandler.model.BillingCycle
import com.subhandler.model.CustomCycleUnit
import com.subhandler.model.Subscription

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val cost: Double,
    val currency: String,
    val billingCycle: String,
    val customCycleAmount: Int?,
    val customCycleUnit: String?,
    val nextRenewalDate: String,
    val autoRenew: Boolean,
    val reminderDays: Int,
    val color: String,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val notificationAlarmId: Int?
)

fun SubscriptionEntity.toDomain() = Subscription(
    id = id,
    name = name,
    cost = cost,
    currency = currency,
    billingCycle = BillingCycle.fromString(billingCycle),
    customCycleAmount = customCycleAmount,
    customCycleUnit = if (customCycleUnit != null) CustomCycleUnit.fromString(customCycleUnit) else null,
    nextRenewalDate = nextRenewalDate,
    autoRenew = autoRenew,
    reminderDays = reminderDays,
    color = color,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt,
    notificationAlarmId = notificationAlarmId
)

fun Subscription.toEntity() = SubscriptionEntity(
    id = id,
    name = name,
    cost = cost,
    currency = currency,
    billingCycle = billingCycle.name,
    customCycleAmount = customCycleAmount,
    customCycleUnit = customCycleUnit?.name,
    nextRenewalDate = nextRenewalDate,
    autoRenew = autoRenew,
    reminderDays = reminderDays,
    color = color,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt,
    notificationAlarmId = notificationAlarmId
)
