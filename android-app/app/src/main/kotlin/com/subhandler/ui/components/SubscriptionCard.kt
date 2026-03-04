package com.subhandler.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.subhandler.model.Subscription
import com.subhandler.utils.*

@Composable
fun SubscriptionCard(
    subscription: Subscription,
    onOpen: (Subscription) -> Unit,
    onRenew: (Subscription) -> Unit
) {
    val days = getDaysUntil(subscription.nextRenewalDate)
    val urgency = getUrgency(days)
    val subColor = parseHexColor(subscription.color)
    var showRenewConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen(subscription) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Left color bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(subColor)
            )

            // Content row
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Name + cost
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subscription.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                    Text(
                        text = formatSubscriptionCost(subscription),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Urgency + date + optional renew button (right-aligned)
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                    }
                    Text(
                        text = formatDate(subscription.nextRenewalDate),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (!subscription.autoRenew) {
                        Spacer(Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showRenewConfirm = true },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                            modifier = Modifier.height(26.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp, MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "Mark Renewed",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
    }

    if (showRenewConfirm) {
        AlertDialog(
            onDismissRequest = { showRenewConfirm = false },
            title = { Text("Mark as Renewed?") },
            text = { Text("This will advance the renewal date for \"${subscription.name}\" by one billing cycle.") },
            confirmButton = {
                TextButton(onClick = {
                    onRenew(subscription)
                    showRenewConfirm = false
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showRenewConfirm = false }) { Text("Cancel") }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}
