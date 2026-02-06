package com.example.collegeschedulehomyakov.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.collegeschedulehomyakov.data.dto.GroupDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownWithSearch(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    onGroupSelected: (GroupDto) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
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

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
            if (!expanded) {
                keyboardController?.hide()
            }
        },
        modifier = modifier
    ) {
        // Поле поиска
        OutlinedTextField(
            value = selectedGroup?.groupName ?: searchText,
            onValueChange = {
                searchText = it
                if (!expanded) expanded = true
            },
            label = { Text("Поиск группы") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    if (filteredGroups.size == 1) {
                        onGroupSelected(filteredGroups[0])
                        searchText = ""
                        expanded = false
                    }
                }
            ),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        )

        // Выпадающее меню
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (filteredGroups.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Группа не найдена",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    onClick = { expanded = false }
                )
            } else {
                filteredGroups.forEach { group ->
                    DropdownMenuItem(
                        text = {
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
                        },
                        onClick = {
                            onGroupSelected(group)
                            searchText = ""
                            expanded = false
                            keyboardController?.hide()
                        }
                    )
                }
            }
        }
    }
}