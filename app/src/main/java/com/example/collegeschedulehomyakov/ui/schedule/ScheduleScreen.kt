package com.example.collegeschedulehomyakov.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedulehomyakov.data.dto.GroupDto
import com.example.collegeschedulehomyakov.data.dto.ScheduleByDateDto
import com.example.collegeschedulehomyakov.data.repository.FavoritesRepository
import com.example.collegeschedulehomyakov.data.network.RetrofitInstance
import com.example.collegeschedulehomyakov.ui.components.DropdownWithSearch
import com.example.collegeschedulehomyakov.ui.components.SimpleLessonCard
import com.example.collegeschedulehomyakov.utils.getWeekDateRange
import kotlinx.coroutines.launch
import android.util.Log

@Composable
fun ScheduleScreen(
    favoritesRepository: FavoritesRepository,
    initialGroup: String? = null,
    onGroupSelected: (String) -> Unit = {}
) {
    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    // Загрузка групп при старте
    LaunchedEffect(Unit) {
        Log.d("ScheduleScreen", "загрузка групп")
        try {
            groups = RetrofitInstance.api.getAllGroups()
            Log.d("ScheduleScreen", "Группы загружены: ${groups.size} шт")
            groups.forEachIndexed { index, group ->
                Log.d("ScheduleScreen", "Группа $index: ${group.groupName}")
            }
            error = null
        } catch (e: Exception) {
            Log.e("ScheduleScreen", "Ошибка загрузки групп", e)
            error = "Ошибка загрузки групп: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // Если передана начальная группа из избранного
    LaunchedEffect(initialGroup, groups) {
        if (initialGroup != null && groups.isNotEmpty()) {
            Log.d("ScheduleScreen", "Ищем группу из избранного: $initialGroup")
            val group = groups.find { it.groupName == initialGroup }
            if (group != null) {
                Log.d("ScheduleScreen", "Найдена группа из избранного: ${group.groupName}")
                selectedGroup = group
            } else {
                Log.d("ScheduleScreen", "Группа $initialGroup не найдена в списке")
            }
        }
    }

    // Загрузка расписания при выборе группы
    LaunchedEffect(selectedGroup) {
        selectedGroup?.let { group ->
            Log.d("ScheduleScreen", "Выбрана группа: ${group.groupName}, начинаем загрузку расписания")
            isLoading = true
            error = null

            try {
                val (start, end) = getWeekDateRange()
                Log.d("ScheduleScreen", "Запрашиваем расписание для: ${group.groupName}")
                Log.d("ScheduleScreen", "Даты: $start - $end")

                schedule = RetrofitInstance.api.getSchedule(
                    groupName = group.groupName,
                    start = start,
                    end = end
                )

                Log.d("ScheduleScreen", "Расписание загружено: ${schedule.size} дней")
                schedule.forEachIndexed { index, day ->
                    Log.d("ScheduleScreen", "День $index: ${day.lessonDate}, пар: ${day.lessons.size}")
                }

                error = null
            } catch (e: Exception) {
                Log.e("ScheduleScreen", "Ошибка загрузки расписания", e)
                error = when {
                    e is java.net.ConnectException -> "Ошибка подключения к серверу"
                    e is java.net.SocketTimeoutException -> "Таймаут подключения"
                    e is retrofit2.HttpException -> "Ошибка HTTP ${e.code()}: ${e.message()}"
                    else -> "Ошибка: ${e.localizedMessage ?: e.toString()}"
                }
                schedule = emptyList()
            } finally {
                isLoading = false
                Log.d("ScheduleScreen", "Загрузка завершена, ошибка: $error")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Покажем отладочную информацию
        if (error != null || isLoading) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    if (error != null) {
                        Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
                    }
                    Text("Группы в списке: ${groups.size}")
                    Text("Выбрана группа: ${selectedGroup?.groupName ?: "нет"}")
                    Text("Загружено дней расписания: ${schedule.size}")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Заголовок и выбор группы
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Расписание",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
        }

        // Выбор группы
        DropdownWithSearch(
            groups = groups,
            selectedGroup = selectedGroup,
            onGroupSelected = { group ->
                Log.d("ScheduleScreen", "Пользователь выбрал группу: ${group.groupName}")
                selectedGroup = group
                onGroupSelected(group.groupName)
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Кнопка избранного и информация о группе
        selectedGroup?.let { group ->
            val isFavorite by favoritesRepository
                .isFavorite(group.groupName)
                .collectAsState(initial = false)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.groupName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "${group.course} курс • ${group.specialty}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = {
                        scope.launch {
                            if (isFavorite) {
                                favoritesRepository.removeFavorite(group.groupName)
                            } else {
                                favoritesRepository.addFavorite(group.groupName)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Состояния загрузки
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Загружаем расписание...")
                        selectedGroup?.let {
                            Text("Группа: ${it.groupName}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "⚠️",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Попробуйте выбрать другую группу",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            selectedGroup == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "👆",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Выберите группу для отображения расписания",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (groups.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Доступно групп: ${groups.size}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            schedule.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "📭",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Расписание не найдено")
                        Text(
                            "На выбранные даты нет занятий",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        selectedGroup?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Группа: ${it.groupName}")
                            Text("Даты: ${getWeekDateRange()}")
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(schedule) { daySchedule ->
                        DayScheduleCard(daySchedule)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScheduleCard(daySchedule: ScheduleByDateDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${daySchedule.weekday}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = daySchedule.lessonDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (daySchedule.lessons.isEmpty()) {
                Text(
                    text = "Нет занятий",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    daySchedule.lessons.forEach { lesson ->
                        SimpleLessonCard(lesson)
                    }
                }
            }
        }
    }
}



