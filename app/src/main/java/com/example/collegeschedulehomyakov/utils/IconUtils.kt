package com.example.collegeschedulehomyakov.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

// Простые иконки для предметов
fun getSubjectIcon(subject: String): String {
    return when {
        subject.contains("англ", ignoreCase = true) -> "🌐"
        subject.contains("мат", ignoreCase = true) -> "🧮"
        subject.contains("физик", ignoreCase = true) -> "⚛️"
        subject.contains("информат", ignoreCase = true) -> "💻"
        subject.contains("физкульт", ignoreCase = true) -> "🏃"
        subject.contains("истори", ignoreCase = true) -> "📜"
        subject.contains("хими", ignoreCase = true) -> "🧪"
        subject.contains("биолог", ignoreCase = true) -> "🔬"
        else -> "📚"
    }
}