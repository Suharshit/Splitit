package com.example.splitit.data

data class Bill(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val totalAmount: Double,
    val numberOfPeople: Int,
    val peopleNames: List<String> = emptyList()
) {
    val amountPerPerson: Double
        get() = if (numberOfPeople > 0) totalAmount / numberOfPeople else 0.0
}