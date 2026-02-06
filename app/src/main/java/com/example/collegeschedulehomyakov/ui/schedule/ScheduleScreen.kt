package com.example.collegeschedulehomyakov.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.collegeschedulehomyakov.data.dto.GroupDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDropdown(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    onGroupSelected: (GroupDto) -> Unit,  // ← ИСПРАВЛЕНО: (GroupDto) вместо (ERROR)
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Фильтруем группы по поисковому тексту
    val filteredGroups = if (searchText.isBlank()) {
        groups
    } else {
        groups.filter { group ->
            group.groupName.contains(searchText, ignoreCase = true) ||
                    group.specialty.contains(searchText, ignoreCase = true)
        }
    }

    Column(modifier = modifier) {
        // Поле поиска
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                if (it.isNotBlank() && !isExpanded) {
                    isExpanded = true
                }
            },
            label = { Text("Поиск группы") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    if (filteredGroups.size == 1) {
                        onGroupSelected(filteredGroups[0])
                        searchText = filteredGroups[0].groupName
                        isExpanded = false
                    }
                }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Список групп (показывается только при поиске)
        if (isExpanded && searchText.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    if (filteredGroups.isEmpty()) {
                        item {
                            Text(
                                text = "Группа не найдена",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(filteredGroups) { group ->
                            GroupItem(
                                group = group,
                                onClick = {
                                    onGroupSelected(group)  // Передаём выбранную группу
                                    searchText = group.groupName
                                    isExpanded = false
                                    keyboardController?.hide()
                                }
                            )
                        }
                    }
                }
            }
        }

        // Выбранная группа (отображается отдельно)
        selectedGroup?.let { group ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Выбрана группа:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = group.groupName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${group.course} курс • ${group.specialty}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = "✓",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun GroupItem(
    group: GroupDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = group.groupName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${group.course} курс • ${group.specialty}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}