package com.subhandler.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.subhandler.model.*
import com.subhandler.ui.theme.PresetColorHexes
import com.subhandler.utils.formatDate
import com.subhandler.utils.parseDate
import com.subhandler.utils.todayString
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionFormSheet(
    existing: Subscription?,
    onSave: (Subscription) -> Unit,
    onDismiss: () -> Unit,
    onDelete: ((Long) -> Unit)? = null,
    defaultCurrency: String = "USD",
    defaultReminderDays: Int = 7,
    defaultBillingCycle: BillingCycle = BillingCycle.MONTHLY
) {
    val isEditing = existing != null
    val title = if (isEditing) "Edit Subscription" else "Add Subscription"

    // Delete confirmation state
    var showDeleteConfirm by remember { mutableStateOf(false) }

    // Form state — use defaults for new subscriptions
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var cost by remember { mutableStateOf(existing?.cost?.toString() ?: "") }
    var currency by remember { mutableStateOf(existing?.currency ?: defaultCurrency) }
    var billingCycle by remember { mutableStateOf(existing?.billingCycle ?: defaultBillingCycle) }
    var customAmount by remember { mutableStateOf(existing?.customCycleAmount?.toString() ?: "1") }
    var customUnit by remember { mutableStateOf(existing?.customCycleUnit ?: CustomCycleUnit.MONTHS) }
    var nextRenewalDate by remember {
        mutableStateOf(existing?.nextRenewalDate ?: LocalDate.now().plusMonths(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
    }
    var autoRenew by remember { mutableStateOf(existing?.autoRenew ?: true) }
    var reminderDays by remember { mutableStateOf(existing?.reminderDays?.toString() ?: defaultReminderDays.toString()) }
    var selectedColor by remember { mutableStateOf(existing?.color ?: PresetColorHexes[0]) }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }

    // Date picker state
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = parseDate(nextRenewalDate)
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    )

    // Validation
    var nameError by remember { mutableStateOf(false) }
    var costError by remember { mutableStateOf(false) }

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
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Name
            FormLabel("Service Name *")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                placeholder = { Text("e.g. Netflix") },
                isError = nameError,
                supportingText = if (nameError) {{ Text("Name is required") }} else null,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedFieldColors(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Cost + Currency row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(2f)) {
                    FormLabel("Cost *")
                    OutlinedTextField(
                        value = cost,
                        onValueChange = { cost = it; costError = false },
                        placeholder = { Text("9.99") },
                        isError = costError,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedFieldColors(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    FormLabel("Currency")
                    DropdownField(
                        value = currency,
                        options = CURRENCIES,
                        onSelect = { currency = it }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Billing Cycle
            FormLabel("Billing Cycle")
            DropdownField(
                value = billingCycle.label,
                options = BillingCycle.entries.map { it.label },
                onSelect = { label ->
                    billingCycle = BillingCycle.entries.first { it.label == label }
                }
            )

            // Custom cycle fields
            if (billingCycle == BillingCycle.CUSTOM) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        FormLabel("Every")
                        OutlinedTextField(
                            value = customAmount,
                            onValueChange = { customAmount = it },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            colors = outlinedFieldColors(),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        FormLabel("Unit")
                        DropdownField(
                            value = customUnit.label,
                            options = CustomCycleUnit.entries.map { it.label },
                            onSelect = { label ->
                                customUnit = CustomCycleUnit.entries.first { it.label == label }
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Next Renewal Date
            FormLabel("Next Renewal Date *")
            OutlinedTextField(
                value = formatDate(nextRenewalDate),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    TextButton(onClick = { showDatePicker = true }) {
                        Text("Change", style = MaterialTheme.typography.labelSmall)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedFieldColors(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Auto Renew toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Auto Renew",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (autoRenew) "Renews automatically" else "Manual renewal needed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = autoRenew,
                    onCheckedChange = { autoRenew = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            // Reminder days
            FormLabel("Reminder Days (0 = disabled)")
            OutlinedTextField(
                value = reminderDays,
                onValueChange = { reminderDays = it },
                placeholder = { Text("7") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedFieldColors(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Color picker
            FormLabel("Color")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PresetColorHexes.forEach { hex ->
                    val color = parseHexColor(hex)
                    val isSelected = hex == selectedColor
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (isSelected) Modifier.border(2.dp, Color.White, CircleShape)
                                else Modifier
                            )
                            .clickable { selectedColor = hex }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Notes
            FormLabel("Notes (optional)")
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("Account info, etc.") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedFieldColors(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Save button
            Button(
                onClick = {
                    nameError = name.isBlank()
                    costError = cost.toDoubleOrNull() == null || cost.toDoubleOrNull()!! < 0
                    if (nameError || costError) return@Button

                    val sub = Subscription(
                        id = existing?.id ?: 0,
                        name = name.trim(),
                        cost = cost.toDoubleOrNull() ?: 0.0,
                        currency = currency,
                        billingCycle = billingCycle,
                        customCycleAmount = if (billingCycle == BillingCycle.CUSTOM)
                            customAmount.toIntOrNull()?.coerceAtLeast(1) else null,
                        customCycleUnit = if (billingCycle == BillingCycle.CUSTOM) customUnit else null,
                        nextRenewalDate = nextRenewalDate,
                        autoRenew = autoRenew,
                        reminderDays = reminderDays.toIntOrNull()?.coerceAtLeast(0) ?: 0,
                        color = selectedColor,
                        notes = notes.trim().ifEmpty { null },
                        createdAt = existing?.createdAt ?: System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    onSave(sub)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isEditing) "Save Changes" else "Add Subscription")
            }

            // Delete button — only visible when editing
            if (isEditing && onDelete != null) {
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Delete Subscription",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteConfirm && existing != null && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Subscription") },
            text = { Text("Are you sure you want to delete \"${existing.name}\"? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(existing.id)
                    showDeleteConfirm = false
                    onDismiss()
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        nextRenewalDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
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
            colors = outlinedFieldColors(),
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
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
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
