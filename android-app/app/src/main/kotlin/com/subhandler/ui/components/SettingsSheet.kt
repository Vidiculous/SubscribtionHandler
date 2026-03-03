package com.subhandler.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.subhandler.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    current: AppSettings,
    onSave: (AppSettings) -> Unit,
    onDismiss: () -> Unit
) {
    var defaultCurrency by remember { mutableStateOf(current.defaultCurrency) }
    var defaultReminderDays by remember { mutableStateOf(current.defaultReminderDays.toString()) }
    var defaultBillingCycle by remember { mutableStateOf(current.defaultBillingCycle) }
    var notificationHour by remember { mutableStateOf(current.notificationHour) }
    var upcomingWindowDays by remember { mutableStateOf(current.upcomingWindowDays.toString()) }
    var sortOrder by remember { mutableStateOf(current.sortOrder) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .imePadding()
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Default Currency
            SettingsLabel("Default Currency")
            SettingsDropdown(
                value = defaultCurrency,
                options = CURRENCIES,
                onSelect = { defaultCurrency = it }
            )

            Spacer(Modifier.height(12.dp))

            // Default Reminder Days
            SettingsLabel("Default Reminder Days (0 = disabled)")
            OutlinedTextField(
                value = defaultReminderDays,
                onValueChange = { defaultReminderDays = it },
                placeholder = { Text("7") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = settingsFieldColors(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Default Billing Cycle
            SettingsLabel("Default Billing Cycle")
            SettingsDropdown(
                value = defaultBillingCycle.label,
                options = BillingCycle.entries.map { it.label },
                onSelect = { label ->
                    defaultBillingCycle = BillingCycle.entries.first { it.label == label }
                }
            )

            Spacer(Modifier.height(12.dp))

            // Notification Hour
            SettingsLabel("Notification Time")
            SettingsDropdown(
                value = hourLabel(notificationHour),
                options = (0..23).map { hourLabel(it) },
                onSelect = { label ->
                    notificationHour = (0..23).first { hourLabel(it) == label }
                }
            )

            Spacer(Modifier.height(12.dp))

            // Upcoming Window
            SettingsLabel("Upcoming Renewal Window")
            SettingsDropdown(
                value = "${upcomingWindowDays} days",
                options = listOf("3", "7", "14", "30").map { "$it days" },
                onSelect = { label ->
                    upcomingWindowDays = label.removeSuffix(" days")
                }
            )

            Spacer(Modifier.height(12.dp))

            // Sort Order
            SettingsLabel("Sort Subscriptions By")
            SettingsDropdown(
                value = sortOrder.label,
                options = SortOrder.entries.map { it.label },
                onSelect = { label ->
                    sortOrder = SortOrder.entries.first { it.label == label }
                }
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    onSave(
                        AppSettings(
                            defaultCurrency = defaultCurrency,
                            defaultReminderDays = defaultReminderDays.toIntOrNull()
                                ?.coerceIn(0, 30) ?: 7,
                            defaultBillingCycle = defaultBillingCycle,
                            notificationHour = notificationHour,
                            upcomingWindowDays = upcomingWindowDays.toIntOrNull()
                                ?.coerceIn(1, 30) ?: 7,
                            sortOrder = sortOrder
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Save Settings")
            }
        }
    }
}

private fun hourLabel(hour: Int): String = when {
    hour == 0  -> "12:00 AM (midnight)"
    hour < 12  -> "$hour:00 AM"
    hour == 12 -> "12:00 PM (noon)"
    else       -> "${hour - 12}:00 PM"
}

@Composable
private fun SettingsLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDropdown(
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = settingsFieldColors(),
            shape = RoundedCornerShape(8.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = MaterialTheme.colorScheme.onSurface) },
                    onClick = { onSelect(option); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun settingsFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
)
