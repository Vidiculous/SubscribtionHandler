package com.subhandler.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.subhandler.model.Subscription
import com.subhandler.ui.theme.*
import com.subhandler.utils.*

@Composable
fun UpcomingRenewals(
    upcoming: List<Subscription>,
    windowDays: Int = 7,
    modifier: Modifier = Modifier
) {
    if (upcoming.isEmpty()) return

    val sectionTitle = when (windowDays) {
        3    -> "Renewing in 3 Days"
        7    -> "Renewing This Week"
        14   -> "Renewing in 2 Weeks"
        30   -> "Renewing This Month"
        else -> "Renewing Soon"
    }

    Column(modifier = modifier) {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(upcoming, key = { it.id }) { sub ->
                UpcomingCard(sub)
            }
        }
    }
}

@Composable
private fun UpcomingCard(subscription: Subscription) {
    val days = getDaysUntil(subscription.nextRenewalDate)
    val urgency = getUrgency(days)
    val urgencyColor = urgencyColor(urgency)
    val initial = subscription.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    val subColor = parseHexColor(subscription.color)

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.width(120.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(subColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(urgencyColor)
                        .align(Alignment.TopEnd)
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = subscription.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatSubscriptionCost(subscription),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatDaysUntil(days),
                style = MaterialTheme.typography.labelSmall,
                color = urgencyColor
            )
        }
    }
}

fun urgencyColor(urgency: Urgency): Color = when (urgency) {
    Urgency.OVERDUE  -> UrgencyOverdue
    Urgency.CRITICAL -> UrgencyCritical
    Urgency.WARNING  -> UrgencyWarning
    Urgency.UPCOMING -> UrgencyUpcoming
    Urgency.OK       -> UrgencyOk
}

fun parseHexColor(hex: String): Color = try {
    Color(android.graphics.Color.parseColor(hex))
} catch (e: Exception) {
    Color(0xFF6366F1)
}
