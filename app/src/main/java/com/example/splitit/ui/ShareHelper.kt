package com.example.splitit.ui

import android.content.Context
import android.content.Intent
import com.example.splitit.data.Bill

fun shareBill(context: Context, bill: Bill) {
    val peopleLines = bill.peopleNames.ifEmpty {
        List(bill.numberOfPeople) { "Person ${it + 1}" }
    }.joinToString("\n") { name ->
        "  $name — ₹${"%.2f".format(bill.amountPerPerson)}"
    }

    val text = """
        💸 ${bill.title}
        
        Total: ₹${"%.2f".format(bill.totalAmount)}
        Split ${bill.numberOfPeople} ways:
        
        $peopleLines
        
        Shared via SplitIt
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share bill via"))
}