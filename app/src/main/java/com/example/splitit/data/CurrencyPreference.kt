package com.example.splitit.data

import android.content.Context

object CurrencyPreference {
    private const val PREFS_NAME = "splitit_prefs"
    private const val KEY_CURRENCY = "currency_symbol"
    private const val DEFAULT_CURRENCY = "₹"

    fun getCurrency(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENCY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
    }

    fun setCurrency(context: Context, symbol: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CURRENCY, symbol)
            .apply()
    }

    val availableCurrencies = listOf(
        "₹" to "Indian Rupee (₹)",
        "$" to "US Dollar ($)",
        "€" to "Euro (€)",
        "£" to "British Pound (£)",
        "¥" to "Japanese Yen (¥)",
        "₩" to "Korean Won (₩)",
        "A$" to "Australian Dollar (A$)",
        "C$" to "Canadian Dollar (C$)"
    )
}