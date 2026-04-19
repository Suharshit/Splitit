package com.example.splitit.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.splitit.data.Bill
import com.example.splitit.viewmodel.BillViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material.icons.filled.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillListScreen(
    viewModel: BillViewModel,
    onAddBill: () -> Unit,
    onBillClick: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    scrollToTop: Boolean = false,
    onScrollToTopDone: () -> Unit = {}
) {
    val bills by viewModel.bills.collectAsState()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val currency by viewModel.currency.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SplitIt",
                            style = MaterialTheme.typography.titleLarge
                        )
                        if (bills.isNotEmpty()) {
                            Text(
                                text = "${bills.size} ${if (bills.size == 1) "bill" else "bills"} · ${currency}${"%.2f".format(bills.sumOf { it.totalAmount })} total",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBill) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Bill")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        if (bills.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No bills yet.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap + to add your first bill",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                state = listState,
            ) {
                items(bills, key = { it.id }) { bill ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteBill(bill)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "\"${bill.title}\" deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.restoreBill(bill)
                                    }
                                }
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            val color by animateColorAsState(
                                targetValue = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
                                    MaterialTheme.colorScheme.errorContainer
                                else Color.Transparent,
                                label = "swipe_bg"
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(end = 24.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    ) {
                        Card(
                            onClick = { onBillClick(bill.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                ListItem(
                                    leadingContent = {
                                        Surface(
                                            shape = MaterialTheme.shapes.small,
                                            modifier = Modifier.size(40.dp),
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    bill.category.emoji,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            }
                                        }
                                    },
                                    headlineContent = {
                                        Text(bill.title, style = MaterialTheme.typography.titleMedium)
                                    },
                                    supportingContent = {
                                        Text("${bill.category.label} · ${bill.numberOfPeople} people")
                                    },
                                    trailingContent = {
                                        Column(
                                            horizontalAlignment = Alignment.End,
                                            modifier = Modifier.padding(top = 20.dp)
                                        ) {
                                            Text(
                                                "$currency${"%.2f".format(bill.totalAmount)}",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                "$currency${"%.2f".format(bill.amountPerPerson)} each",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                )

                                // Payment status badge — top right corner
                                val paidCount = bill.paidCount
                                val total = bill.numberOfPeople
                                val allPaid = bill.allPaid

                                if (total > 0) {
                                    Surface(
                                        shape = MaterialTheme.shapes.small,
                                        color = when {
                                            allPaid -> MaterialTheme.colorScheme.primaryContainer
                                            paidCount > 0 -> MaterialTheme.colorScheme.tertiaryContainer
                                            else -> MaterialTheme.colorScheme.errorContainer
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(top = 8.dp, end = 8.dp)
                                    ) {
                                        Text(
                                            text = when {
                                                allPaid -> "Settled"
                                                paidCount > 0 -> "$paidCount/$total paid"
                                                else -> "Unpaid"
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = when {
                                                allPaid -> MaterialTheme.colorScheme.onPrimaryContainer
                                                paidCount > 0 -> MaterialTheme.colorScheme.onTertiaryContainer
                                                else -> MaterialTheme.colorScheme.onErrorContainer
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}