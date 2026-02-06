package com.example.collegeschedulehomyakov.ui.components

import androidx.compose.foundation.layout.*
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
    // Ищем данные в groupParts
    val lessonInfo = when {
        // Если есть FULL группа
        lesson.groupParts[LessonGroupPart.FULL] != null -> {
            val full = lesson.groupParts[LessonGroupPart.FULL]!!
            LessonDisplayInfo(
                subject = full.subject,
                teacher = full.teacher,
                teacherPosition = full.teacherPosition,
                classroom = full.classroom,
                building = full.building,
                address = full.address,
                hasSubgroups = false
            )
        }
        // Если есть подгруппы
        lesson.groupParts.any { it.value != null } -> {
            val firstValidPart = lesson.groupParts.values.firstOrNull { it != null }
            firstValidPart?.let { part ->
                LessonDisplayInfo(
                    subject = part.subject,
                    teacher = part.teacher,
                    teacherPosition = part.teacherPosition,
                    classroom = part.classroom,
                    building = part.building,
                    address = part.address,
                    hasSubgroups = true
                )
            }
        }
        // Нет данных
        else -> {
            LessonDisplayInfo(
                subject = "Не указано",
                teacher = "",
                teacherPosition = "",
                classroom = "",
                building = "",
                address = "",
                hasSubgroups = false
            )
        }
    } ?: LessonDisplayInfo(
        subject = "Нет информации",
        teacher = "",
        teacherPosition = "",
        classroom = "",
        building = "",
        address = "",
        hasSubgroups = false
    )

    // Отображаем карточку
    LessonCardContent(
        number = lesson.lessonNumber,
        time = lesson.time,
        info = lessonInfo
    )
}

@Composable
private fun LessonCardContent(
    number: Int,
    time: String,
    info: LessonDisplayInfo
) {
    val buildingColor = getBuildingColor(info.building)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть: номер и время
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(70.dp)
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = buildingColor
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall
                )
                if (info.hasSubgroups) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "👥",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Иконка предмета (эмодзи)
            Text(
                text = getSubjectIcon(info.subject),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Информация
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = info.subject,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                if (info.teacher.isNotBlank()) {
                    Text(
                        text = info.teacher,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (info.teacherPosition.isNotBlank()) {
                    Text(
                        text = info.teacherPosition,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (info.building.isNotBlank() && info.classroom.isNotBlank()) {
                    Text(
                        text = "${info.building}, ауд. ${info.classroom}",
                        style = MaterialTheme.typography.bodySmall,
                        color = buildingColor
                    )
                }

                if (info.address.isNotBlank()) {
                    Text(
                        text = info.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Вспомогательный класс для отображения
private data class LessonDisplayInfo(
    val subject: String,
    val teacher: String,
    val teacherPosition: String,
    val classroom: String,
    val building: String,
    val address: String,
    val hasSubgroups: Boolean
)

// Дополнительная карточка для отображения всех подгрупп (опционально)
@Composable
fun DetailedLessonCard(lesson: com.example.collegeschedulehomyakov.data.dto.LessonDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Заголовок: номер пары и время
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Пара ${lesson.lessonNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = lesson.time,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Отображаем каждую подгруппу
            lesson.groupParts.forEach { (part, info) ->
                if (info != null) {
                    SubgroupInfo(part = part, info = info)
                }
            }
        }
    }
}

@Composable
fun SubgroupInfo(part: LessonGroupPart, info: com.example.collegeschedulehomyakov.data.dto.LessonPartDto) {
    val buildingColor = getBuildingColor(info.building)

    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        // Название подгруппы
        Text(
            text = when (part) {
                LessonGroupPart.FULL -> "Вся группа"
                LessonGroupPart.SUB1 -> "Подгруппа 1"
                LessonGroupPart.SUB2 -> "Подгруппа 2"
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Информация о занятии
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getSubjectIcon(info.subject),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = info.subject,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = info.teacher,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${info.building}, ауд. ${info.classroom}",
                    style = MaterialTheme.typography.bodySmall,
                    color = buildingColor
                )
            }
        }
    }
}