package com.subhandler.model

data class AppSettings(
    val defaultCurrency: String = "USD",
    val defaultReminderDays: Int = 7,
    val defaultBillingCycle: BillingCycle = BillingCycle.MONTHLY,
    val notificationHour: Int = 9,
    val upcomingWindowDays: Int = 7,
    val sortOrder: SortOrder = SortOrder.DATE
)

enum class SortOrder(val label: String) {
    DATE("Renewal Date"),
    NAME("Name"),
    COST("Cost")
}
