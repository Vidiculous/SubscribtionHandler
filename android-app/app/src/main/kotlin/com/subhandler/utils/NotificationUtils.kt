package com.subhandler.utils

import com.subhandler.model.Subscription

fun buildNotificationTitle(subscription: Subscription, daysUntil: Long): String = when {
    daysUntil <= 0 -> "${subscription.name} renews today"
    daysUntil == 1L -> "${subscription.name} renews tomorrow"
    else -> "${subscription.name} renews in $daysUntil days"
}

fun buildNotificationBody(subscription: Subscription, daysUntil: Long): String {
    val cost = formatSubscriptionCost(subscription)
    return when {
        daysUntil <= 0 -> "${subscription.name} renews today — $cost"
        daysUntil == 1L -> "${subscription.name} renews tomorrow — $cost"
        else -> "${subscription.name} renews in $daysUntil days — $cost"
    }
}
