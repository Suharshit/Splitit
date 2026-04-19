package com.example.splitit.data

import androidx.compose.ui.graphics.Color

enum class BillCategory(
    val label: String,
    val emoji: String,
    val color: Color
) {
    FOOD("Food", "🍔", Color(0xFFE57373)),
    TRAVEL("Travel", "✈️", Color(0xFF64B5F6)),
    RENT("Rent", "🏠", Color(0xFF81C784)),
    SHOPPING("Shopping", "🛍️", Color(0xFFFFD54F)),
    ENTERTAINMENT("Entertainment", "🎬", Color(0xFFBA68C8)),
    UTILITIES("Utilities", "💡", Color(0xFF4DB6AC)),
    HEALTH("Health", "💊", Color(0xFFF06292)),
    OTHER("Other", "📋", Color(0xFFB0BEC5))
}