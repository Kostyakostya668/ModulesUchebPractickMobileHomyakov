package com.example.collegeschedulehomyakov

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.collegeschedulehomyakov.ui.components.DropdownTestScreen
import com.example.collegeschedulehomyakov.ui.theme.CollegeScheduleHomyakovTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollegeScheduleHomyakovTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DropdownTestScreen() // Тестируем выпадающий список
                }
            }
        }
    }
}