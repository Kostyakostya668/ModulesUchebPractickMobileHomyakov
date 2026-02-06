package com.example.collegeschedulehomyakov.utils

import androidx.compose.ui.graphics.Color

// Простые цвета для корпусов
val buildingColors = mapOf(
    "Главный" to Color(0xFF4CAF50),      // Зелёный
    "Учебный" to Color(0xFF2196F3),      // Синий
    "Лабораторный" to Color(0xFF9C27B0), // Фиолетовый
    "Спортивный" to Color(0xFFFF9800),   // Оранжевый
    "Библиотечный" to Color(0xFF795548)  // Коричневый
)

fun getBuildingColor(building: String): Color {
    return buildingColors.entries.firstOrNull {
        building.contains(it.key, ignoreCase = true)
    }?.value ?: Color(0xFF607D8B)
}