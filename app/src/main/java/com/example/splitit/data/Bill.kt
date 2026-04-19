package com.example.splitit.data

data class Bill(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val totalAmount: Double,
    val numberOfPeople: Int,
    val peopleNames: List<String> = emptyList(),
    val customAmounts: List<Double> = emptyList(),
    val paidStatus: List<Boolean> = emptyList()
) {
    val amountPerPerson: Double
        get() = if (numberOfPeople > 0) totalAmount / numberOfPeople else 0.0

    fun amountFor(index: Int): Double {
        return if (customAmounts.size > index) customAmounts[index]
        else amountPerPerson
    }

    fun isPaid(index: Int): Boolean {
        return paidStatus.getOrElse(index) { false }
    }

    val allPaid: Boolean
        get() = (0 until numberOfPeople).all { isPaid(it) }

    val paidCount: Int
        get() = (0 until numberOfPeople).count { isPaid(it) }
}