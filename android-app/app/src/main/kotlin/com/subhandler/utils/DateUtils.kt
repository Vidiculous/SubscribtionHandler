package com.subhandler.utils

import com.subhandler.model.BillingCycle
import com.subhandler.model.CustomCycleUnit
import com.subhandler.model.Subscription
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy")

fun parseDate(dateStr: String): LocalDate = LocalDate.parse(dateStr, DATE_FORMAT)

fun formatDate(dateStr: String): String =
    parseDate(dateStr).format(DISPLAY_FORMAT)

fun todayString(): String = LocalDate.now().format(DATE_FORMAT)

fun getDaysUntil(nextRenewalDate: String): Long {
    val today = LocalDate.now()
    val renewal = parseDate(nextRenewalDate)
    return ChronoUnit.DAYS.between(today, renewal)
}

fun formatDaysUntil(days: Long): String = when {
    days < 0  -> "${-days} day${if (-days == 1L) "" else "s"} overdue"
    days == 0L -> "Today"
    days == 1L -> "Tomorrow"
    else       -> "In $days days"
}

enum class Urgency { OVERDUE, CRITICAL, WARNING, UPCOMING, OK }

fun getUrgency(days: Long): Urgency = when {
    days < 0  -> Urgency.OVERDUE
    days == 0L -> Urgency.CRITICAL
    days <= 3 -> Urgency.CRITICAL
    days <= 7 -> Urgency.WARNING
    days <= 14 -> Urgency.UPCOMING
    else       -> Urgency.OK
}

fun advanceByOneCycle(subscription: Subscription): String {
    val date = parseDate(subscription.nextRenewalDate)
    val newDate = when (subscription.billingCycle) {
        BillingCycle.WEEKLY  -> date.plusWeeks(1)
        BillingCycle.MONTHLY -> date.plusMonths(1)
        BillingCycle.YEARLY  -> date.plusYears(1)
        BillingCycle.CUSTOM  -> {
            val amount = subscription.customCycleAmount?.toLong() ?: 1L
            when (subscription.customCycleUnit ?: CustomCycleUnit.MONTHS) {
                CustomCycleUnit.DAYS   -> date.plusDays(amount)
                CustomCycleUnit.WEEKS  -> date.plusWeeks(amount)
                CustomCycleUnit.MONTHS -> date.plusMonths(amount)
            }
        }
    }
    return newDate.format(DATE_FORMAT)
}

/** Returns triggerDate (yyyy-MM-dd) = nextRenewalDate - reminderDays */
fun reminderDate(subscription: Subscription): LocalDate? {
    if (subscription.reminderDays <= 0) return null
    return parseDate(subscription.nextRenewalDate).minusDays(subscription.reminderDays.toLong())
}

fun formatCycleLabel(subscription: Subscription): String = when (subscription.billingCycle) {
    BillingCycle.WEEKLY  -> "/wk"
    BillingCycle.MONTHLY -> "/mo"
    BillingCycle.YEARLY  -> "/yr"
    BillingCycle.CUSTOM  -> {
        val amount = subscription.customCycleAmount ?: 1
        val unit = subscription.customCycleUnit?.label ?: "months"
        "/$amount $unit"
    }
}
