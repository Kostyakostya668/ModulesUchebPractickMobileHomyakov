//package com.example.collegeschedulehomyakov.ui.components
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.collegeschedulehomyakov.data.network.RetrofitInstance
//import com.example.collegeschedulehomyakov.utils.getWeekDateRange
//import kotlinx.coroutines.launch
//
//@Composable
//fun ApiTestScreen() {
//    var testResult by remember { mutableStateOf("Нажмите кнопку для теста") }
//    var isLoading by remember { mutableStateOf(false) }
//    val scope = rememberCoroutineScope()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            "🔍 Тестирование API",
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                isLoading = true
//                scope.launch {
//                    try {
//                        val groups = RetrofitInstance.api.getAllGroups()
//                        var result = "✅ Группы загружены (${groups.size} шт)\n\n"
//
//                        if (groups.isNotEmpty()) {
//                            val firstGroup = groups.first()
//                            val (start, end) = getWeekDateRange()
//
//                            // ПРОБУЕМ ПОЛУЧИТЬ СЫРОЙ ОТВЕТ
//                            val call = RetrofitInstance.api.getSchedule(
//                                groupName = firstGroup.groupName,
//                                start = start,
//                                end = end
//                            )
//
//                            // Если есть доступ к response, выведем структуру
//                            result += "✅ Тестируем расписание для ${firstGroup.groupName}:\n"
//                            result += "Даты: $start - $end\n"
//                            result += "Получено дней: ${call.size}\n\n"
//
//                            // Выведем первый день для анализа структуры
//                            if (call.isNotEmpty()) {
//                                val firstDay = call[0]
//                                result += "СТРУКТУРА первого дня:\n"
//                                result += "- lessonDate: ${firstDay.lessonDate}\n"
//                                result += "- weekday: ${firstDay.weekday}\n"
//                                result += "- lessons.size: ${firstDay.lessons.size}\n"
//
//                                if (firstDay.lessons.isNotEmpty()) {
//                                    val firstLesson = firstDay.lessons[0]
//                                    result += "\nСТРУКТУРА первой пары:\n"
//                                    result += "- lessonNumber: ${firstLesson.lessonNumber}\n"
//                                    result += "- time: ${firstLesson.time}\n"
//                                    result += "- subject: ${firstLesson.subject}\n"
//                                    result += "- teacher: ${firstLesson.teacher}\n"
//                                    result += "- teacherPosition: ${firstLesson.teacherPosition}\n"
//                                    result += "- classroom: ${firstLesson.classroom}\n"
//                                    result += "- building: ${firstLesson.building}\n"
//                                    result += "- address: ${firstLesson.address}\n"
//                                    result += "- groupParts.keys: ${firstLesson.groupParts.keys}\n"
//
//                                    // Проверим groupParts
//                                    firstLesson.groupParts.forEach { (key, value) ->
//                                        result += "\n  groupParts[$key]:\n"
//                                        if (value != null) {
//                                            result += "    - subject: ${value.subject}\n"
//                                            result += "    - teacher: ${value.teacher}\n"
//                                            result += "    - teacherPosition: ${value.teacherPosition}\n"
//                                            result += "    - classroom: ${value.classroom}\n"
//                                            result += "    - building: ${value.building}\n"
//                                            result += "    - address: ${value.address}\n"
//                                        } else {
//                                            result += "    - NULL\n"
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        testResult = result
//                    } catch (e: Exception) {
//                        testResult = "❌ Ошибка: ${e.message}\n${e.stackTraceToString()}"
//                    } finally {
//                        isLoading = false
//                    }
//                }
//            }
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(16.dp),
//                    strokeWidth = 2.dp
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Тестируем...")
//            } else {
//                Text("Запустить тест API")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Card(
//            modifier = Modifier.fillMaxSize(),
//            colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.surfaceVariant
//            )
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                Text(
//                    text = testResult,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}