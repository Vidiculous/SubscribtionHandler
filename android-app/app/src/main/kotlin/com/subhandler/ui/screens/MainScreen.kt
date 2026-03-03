package com.subhandler.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.subhandler.model.Subscription
import com.subhandler.ui.components.*
import com.subhandler.ui.viewmodel.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: SubscriptionViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsState()
    val spendSummary by vm.spendSummary.collectAsState()
    val upcoming by vm.upcomingRenewals.collectAsState()
    val settings by vm.settings.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var editingSubscription by remember { mutableStateOf<Subscription?>(null) }
    var showSettings by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Subscribtor",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingSubscription = null
                    showForm = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add subscription")
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            // Notification permission banner
            item {
                NotificationBanner()
            }

            // Spend summary
            item {
                SpendSummaryRow(summary = spendSummary)
            }

            // Upcoming renewals
            if (upcoming.isNotEmpty()) {
                item {
                    UpcomingRenewals(
                        upcoming = upcoming,
                        windowDays = settings.upcomingWindowDays
                    )
                }
            }

            // Section header
            item {
                Text(
                    text = "All Subscriptions",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Subscription grid — rendered as pairs in LazyColumn rows
            if (uiState.subscriptions.isEmpty()) {
                item {
                    Text(
                        text = "No subscriptions yet.\nTap + to add one.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                    )
                }
            } else {
                val chunked = uiState.subscriptions.chunked(2)
                items(chunked.size) { rowIndex ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val row = chunked[rowIndex]
                        SubscriptionCard(
                            subscription = row[0],
                            onOpen = { editingSubscription = it; showForm = true },
                            onRenew = { vm.renewSubscription(it) },
                            modifier = Modifier.weight(1f)
                        )
                        if (row.size > 1) {
                            SubscriptionCard(
                                subscription = row[1],
                                onOpen = { editingSubscription = it; showForm = true },
                                onRenew = { vm.renewSubscription(it) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    // Add / Edit bottom sheet
    if (showForm) {
        SubscriptionFormSheet(
            existing = editingSubscription,
            onSave = { sub ->
                if (editingSubscription == null) vm.addSubscription(sub)
                else vm.updateSubscription(sub)
                showForm = false
                editingSubscription = null
            },
            onDismiss = {
                showForm = false
                editingSubscription = null
            },
            onDelete = { id ->
                vm.deleteSubscription(id)
            },
            defaultCurrency = settings.defaultCurrency,
            defaultReminderDays = settings.defaultReminderDays,
            defaultBillingCycle = settings.defaultBillingCycle
        )
    }

    // Settings bottom sheet
    if (showSettings) {
        SettingsSheet(
            current = settings,
            onSave = { newSettings ->
                vm.updateSettings(newSettings)
                showSettings = false
            },
            onDismiss = { showSettings = false }
        )
    }
}
