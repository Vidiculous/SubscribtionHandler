package com.subhandler.utils

import com.subhandler.model.BillingCycle
import com.subhandler.model.CustomCycleUnit
import com.subhandler.model.SpendSummary
import com.subhandler.model.Subscription
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.roundToInt

fun toMonthly(subscription: Subscription): Double = when (subscription.billingCycle) {
    BillingCycle.WEEKLY  -> subscription.cost * (52.0 / 12.0)
    BillingCycle.MONTHLY -> subscription.cost
    BillingCycle.YEARLY  -> subscription.cost / 12.0
    BillingCycle.CUSTOM  -> {
        val amount = subscription.customCycleAmount ?: 1
        val daysInCycle = when (subscription.customCycleUnit ?: CustomCycleUnit.MONTHS) {
            CustomCycleUnit.DAYS   -> amount.toDouble()
            CustomCycleUnit.WEEKS  -> amount * 7.0
            CustomCycleUnit.MONTHS -> amount * 30.4375
        }
        subscription.cost * (365.25 / daysInCycle / 12.0)
    }
}

fun toYearly(subscription: Subscription): Double = toMonthly(subscription) * 12.0

fun computeSpendSummary(subscriptions: List<Subscription>): SpendSummary {
    if (subscriptions.isEmpty()) return SpendSummary()
    val monthly = subscriptions.sumOf { toMonthly(it) }
    val yearly = subscriptions.sumOf { toYearly(it) }
    val currencies = subscriptions.map { it.currency }.toSet()
    val currency = if (currencies.size == 1) currencies.first() else "Mixed"
    return SpendSummary(monthly = monthly, yearly = yearly, currency = currency, count = subscriptions.size)
}

fun formatCurrency(amount: Double, currency: String): String {
    if (currency == "Mixed") {
        return "~${"%.2f".format(amount)}"
    }
    return try {
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        format.currency = Currency.getInstance(currency)
        format.maximumFractionDigits = 2
        format.minimumFractionDigits = 2
        format.format(amount)
    } catch (e: Exception) {
        "$currency ${"%.2f".format(amount)}"
    }
}

fun formatSubscriptionCost(subscription: Subscription): String {
    val costStr = "%.2f".format(subscription.cost)
    val cycleLabel = formatCycleLabel(subscription)
    return "${subscription.currency} $costStr$cycleLabel"
}
