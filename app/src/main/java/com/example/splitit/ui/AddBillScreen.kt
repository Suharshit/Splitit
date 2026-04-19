package com.example.splitit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.splitit.viewmodel.BillViewModel
import com.example.splitit.data.BillCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillScreen(
    viewModel: BillViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var totalAmountDisplay by remember { mutableStateOf("") }
    var totalAmountRaw by remember { mutableStateOf("") }
    var people by remember { mutableStateOf(listOf("", "")) }
    var isEqualSplit by remember { mutableStateOf(true) }
    var customAmounts by remember { mutableStateOf(listOf("", "")) }
    var titleError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var customAmountError by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(BillCategory.OTHER) }
    val focusRequester = remember { FocusRequester() }
    val currency by viewModel.currency.collectAsState()

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    // Keep customAmounts list size in sync with people list
    LaunchedEffect(people.size) {
        customAmounts = List(people.size) { customAmounts.getOrElse(it) { "" } }
    }

    val totalParsed = totalAmountRaw.toDoubleOrNull() ?: 0.0
    val customTotal = customAmounts.sumOf { it.toDoubleOrNull() ?: 0.0 }
    val remaining = totalParsed - customTotal

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Bill") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; titleError = false },
                    label = { Text("Bill title (e.g. Dinner, Groceries)") },
                    isError = titleError,
                    supportingText = { if (titleError) Text("Title cannot be empty") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = totalAmountDisplay,
                    onValueChange = { input ->
                        val raw = parseAmount(input)
                        if (raw.isEmpty() || raw.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            totalAmountRaw = raw
                            totalAmountDisplay = if (raw.isEmpty()) "" else formatAmount(raw)
                            amountError = false
                        }
                    },
                    label = { Text("Total amount") },
                    isError = amountError,
                    supportingText = { if (amountError) Text("Enter a valid amount") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix = { Text(currency) }
                )
            }

            // Split type toggle
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = isEqualSplit,
                        onClick = { isEqualSplit = true },
                        label = { Text("Equal split") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = !isEqualSplit,
                        onClick = { isEqualSplit = false },
                        label = { Text("Custom split") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Equal split preview
            if (isEqualSplit) {
                item {
                    val validPeople = people.filter { it.isNotBlank() }
                    if (totalParsed > 0 && validPeople.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Each person pays",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    "$currency${"%.2f".format(totalParsed / validPeople.size)}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // Custom split remaining indicator
            if (!isEqualSplit && totalParsed > 0) {
                item {
                    val remainingColor = when {
                        remaining < 0 -> MaterialTheme.colorScheme.error
                        remaining == 0.0 -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Remaining to assign",
                                style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "$currency${"%.2f".format(remaining)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = remainingColor
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("People", style = MaterialTheme.typography.titleMedium)
                    if (customAmountError) {
                        Text(
                            "Amounts don't add up to total",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            item {
                CategoryPicker(
                    selected = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            itemsIndexed(people) { index, name ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            people = people.toMutableList().also { list -> list[index] = it }
                        },
                        label = { Text("Person ${index + 1}") },
                        modifier = Modifier.weight(if (isEqualSplit) 1f else 0.5f),
                        singleLine = true
                    )
                    if (!isEqualSplit) {
                        OutlinedTextField(
                            value = customAmounts.getOrElse(index) { "" },
                            onValueChange = { input ->
                                val raw = input.filter { it.isDigit() || it == '.' }
                                customAmounts = customAmounts.toMutableList()
                                    .also { list -> list[index] = raw }
                                customAmountError = false
                            },
                            modifier = Modifier.weight(0.5f),
                            label = { Text("$currency Amount") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                    if (people.size > 1) {
                        IconButton(onClick = {
                            people = people.toMutableList().also { it.removeAt(index) }
                            customAmounts = customAmounts.toMutableList().also { it.removeAt(index) }
                        }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        people = people + ""
                        customAmounts = customAmounts + ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add person")
                }
            }

            item {
                Button(
                    onClick = {
                        val parsedAmount = totalAmountRaw.toDoubleOrNull()
                        val filledNames = people.mapIndexed { index, name ->
                            name.ifBlank { "Person ${index + 1}" }
                        }
                        titleError = title.isBlank()
                        amountError = parsedAmount == null || parsedAmount <= 0

                        if (!isEqualSplit) {
                            val customParsed = customAmounts.map { it.toDoubleOrNull() ?: 0.0 }
                            val sum = customParsed.sum()
                            customAmountError = parsedAmount != null &&
                                    Math.abs(sum - parsedAmount) > 0.01
                        }

                        if (!titleError && !amountError && !customAmountError) {
                            val finalAmounts = if (!isEqualSplit)
                                customAmounts.map { it.toDoubleOrNull() ?: 0.0 }
                            else emptyList()

                            viewModel.addBill(
                                title = title.trim(),
                                total = parsedAmount!!,
                                names = filledNames,
                                customAmounts = finalAmounts,
                                category = selectedCategory    // ← add this
                            )
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Save Bill", style = MaterialTheme.typography.titleMedium)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

fun formatAmount(input: String): String {
    val digits = input.filter { it.isDigit() || it == '.' }
    val parts = digits.split(".")
    val intPart = parts[0].trimStart('0').ifEmpty { "0" }
    val formatted = try {
        "%,d".format(intPart.toLong())
    } catch (e: Exception) { intPart }
    return if (parts.size > 1) "$formatted.${parts[1].take(2)}" else formatted
}

fun parseAmount(formatted: String): String = formatted.replace(",", "")