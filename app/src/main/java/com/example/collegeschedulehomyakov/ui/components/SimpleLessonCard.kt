package com.example.collegeschedulehomyakov.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedulehomyakov.data.dto.LessonGroupPart
import com.example.collegeschedulehomyakov.utils.getBuildingColor
import com.example.collegeschedulehomyakov.utils.getSubjectIcon

@Composable
fun SimpleLessonCard(lesson: com.example.collegeschedulehomyakov.data.dto.LessonDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Заголовок: номер пары и время
            LessonHeader(
                number = lesson.lessonNumber,
                time = lesson.time,
                hasSubgroups = lesson.groupParts.size > 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Отображаем ВСЕ подгруппы
            when {
                // 1 Есть занятие для всей группы
                lesson.groupParts[LessonGroupPart.FULL] != null -> {
                    val full = lesson.groupParts[LessonGroupPart.FULL]!!
                    SubgroupItem(
                        partName = "Вся группа",
                        info = full,
                        showDivider = false
                    )
                }

                // 2 Есть подгруппы
                lesson.groupParts.any { it.value != null } -> {
                    // Отображаем каждую не-null подгруппу
                    val validParts = lesson.groupParts.filter { it.value != null }

                    validParts.entries.forEachIndexed { index, (part, info) ->
                        info?.let {
                            SubgroupItem(
                                partName = when (part) {
                                    LessonGroupPart.SUB1 -> "Подгруппа 1"
                                    LessonGroupPart.SUB2 -> "Подгруппа 2"
                                    else -> "Группа"
                                },
                                info = it,
                                showDivider = index < validParts.size - 1
                            )
                        }
                    }
                }

                // 3 Нет данных
                else -> {
                    Text(
                        text = "Нет информации о занятии",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonHeader(
    number: Int,
    time: String,
    hasSubgroups: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Номер пары
        Text(
            text = "$number",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Время и индикатор подгрупп
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium
            )
            if (hasSubgroups) {
                Text(
                    text = "Подгруппы",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SubgroupItem(
    partName: String,
    info: com.example.collegeschedulehomyakov.data.dto.LessonPartDto,
    showDivider: Boolean
) {
    val buildingColor = getBuildingColor(info.building)

    Column {
        // Название подгруппы
        Text(
            text = partName,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Информация о занятии
        Row(
            verticalAlignment = Alignment.Top
        ) {
            // Иконка предмета
            Text(
                text = getSubjectIcon(info.subject),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Детали
            Column(modifier = Modifier.weight(1f)) {
                // Предмет
                Text(
                    text = info.subject,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                // Преподаватель
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = info.teacher,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Должность преподавателя (если есть)
                if (info.teacherPosition.isNotBlank()) {
                    Text(
                        text = info.teacherPosition,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Аудитория
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = buildingColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${info.building}, ауд. ${info.classroom}",
                        style = MaterialTheme.typography.bodySmall,
                        color = buildingColor
                    )
                }

                // Адрес (если есть)
                if (info.address.isNotBlank()) {
                    Text(
                        text = info.address,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Разделитель между подгруппами
        if (showDivider) {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}