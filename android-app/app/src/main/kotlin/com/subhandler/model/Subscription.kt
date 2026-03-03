package com.subhandler.model

data class Subscription(
    val id: Long = 0,
    val name: String,
    val cost: Double,
    val currency: String = "USD",
    val billingCycle: BillingCycle = BillingCycle.MONTHLY,
    val customCycleAmount: Int? = null,
    val customCycleUnit: CustomCycleUnit? = null,
    val nextRenewalDate: String,           // "yyyy-MM-dd"
    val autoRenew: Boolean = true,
    val reminderDays: Int = 7,             // 0 = disabled
    val color: String = "#6366f1",
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val notificationAlarmId: Int? = null   // AlarmManager requestCode
)

val CURRENCIES = listOf("USD", "EUR", "GBP", "SEK", "NOK", "DKK", "CHF", "JPY", "CAD", "AUD")

val PRESET_COLORS = listOf(
    "#6366f1", // indigo
    "#ec4899", // pink
    "#f59e0b", // amber
    "#22c55e", // green
    "#06b6d4", // cyan
    "#8b5cf6", // violet
    "#ef4444", // red
    "#14b8a6", // teal
    "#f97316", // orange
    "#64748b"  // slate
)
