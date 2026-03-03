package com.subhandler.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.subhandler.model.Subscription
import com.subhandler.utils.*

@Composable
fun SubscriptionCard(
    subscription: Subscription,
    onOpen: (Subscription) -> Unit,
    onRenew: (Subscription) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = getDaysUntil(subscription.nextRenewalDate)
    val urgency = getUrgency(days)
    val subColor = parseHexColor(subscription.color)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpen(subscription) }
    ) {
        Column {
            // Color bar at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(subColor)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                // Avatar + name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(subColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = subscription.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = subscription.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Cost
                Text(
                    text = formatSubscriptionCost(subscription),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Renewal date + urgency dot
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(urgencyColor(urgency))
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = formatDaysUntil(days),
                        style = MaterialTheme.typography.labelSmall,
                        color = urgencyColor(urgency)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = formatDate(subscription.nextRenewalDate),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Renew button for manual subscriptions
                if (!subscription.autoRenew) {
                    Spacer(Modifier.height(8.dp))
                    RenewButton(onRenew = { onRenew(subscription) })
                }
            }
        }
    }
}
