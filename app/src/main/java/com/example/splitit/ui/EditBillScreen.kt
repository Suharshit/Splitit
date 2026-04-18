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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.splitit.data.Bill
import com.example.splitit.viewmodel.BillViewModel
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBillScreen(
    bill: Bill,
    viewModel: BillViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf(bill.title) }
    var totalAmount by remember { mutableStateOf(bill.totalAmount.toString()) }
    var people by remember {
        mutableStateOf(
            bill.peopleNames.ifEmpty {
                List(bill.numberOfPeople) { "Person ${it + 1}" }
            }
        )
    }
    var titleError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var peopleError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Bill") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
                    label = { Text("Bill title") },
                    isError = titleError,
                    supportingText = { if (titleError) Text("Title cannot be empty") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),   // ← add this
                    singleLine = true
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }

            item {
                OutlinedTextField(
                    value = totalAmount,
                    onValueChange = { totalAmount = it; amountError = false },
                    label = { Text("Total amount (₹)") },
                    isError = amountError,
                    supportingText = { if (amountError) Text("Enter a valid amount") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }

            // Live split preview
            item {
                val amount = totalAmount.toDoubleOrNull()
                val validPeople = people.filter { it.isNotBlank() }
                if (amount != null && amount > 0 && validPeople.isNotEmpty()) {
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
                                "₹${"%.2f".format(amount / validPeople.size)}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
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
                    if (peopleError) {
                        Text(
                            "Add at least 1 name",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
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
                            peopleError = false
                        },
                        label = { Text("Person ${index + 1}") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    if (people.size > 1) {
                        IconButton(onClick = {
                            people = people.toMutableList().also { it.removeAt(index) }
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
                    onClick = { people = people + "" },
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
                        val parsedAmount = totalAmount.toDoubleOrNull()
                        val validNames = people.filter { it.isNotBlank() }
                        titleError = title.isBlank()
                        amountError = parsedAmount == null || parsedAmount <= 0
                        peopleError = validNames.isEmpty()

                        if (!titleError && !amountError && !peopleError) {
                            viewModel.updateBill(
                                bill.copy(
                                    title = title.trim(),
                                    totalAmount = parsedAmount!!,
                                    numberOfPeople = validNames.size,
                                    peopleNames = validNames
                                )
                            )
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Save Changes", style = MaterialTheme.typography.titleMedium)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}