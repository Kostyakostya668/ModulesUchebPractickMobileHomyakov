//package com.example.collegeschedulehomyakov.ui.components
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.collegeschedulehomyakov.data.dto.GroupDto
//import com.example.collegeschedulehomyakov.data.network.RetrofitInstance
//import kotlinx.coroutines.launch
//
//@Composable
//fun DropdownTestScreen() {
//    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
//    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    var error by remember { mutableStateOf<String?>(null) }
//
//    val scope = rememberCoroutineScope()
//
//    // Загружаем группы
//    LaunchedEffect(Unit) {
//        scope.launch {
//            try {
//                groups = RetrofitInstance.api.getAllGroups()
//                error = null
//            } catch (e: Exception) {
//                error = "Ошибка загрузки: ${e.message}"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Выбор группы",
//            style = MaterialTheme.typography.headlineSmall
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        when {
//            isLoading -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        CircularProgressIndicator()
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text("Загружаем список групп...")
//                    }
//                }
//            }
//
//            error != null -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Text(
//                            text = "❌",
//                            style = MaterialTheme.typography.displayMedium
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text(
//                            text = error!!,
//                            color = MaterialTheme.colorScheme.error
//                        )
//                    }
//                }
//            }
//
//            else -> {
//                // Выпадающий список С ПОИСКОМ
//                DropdownWithSearch(
//                    groups = groups,
//                    selectedGroup = selectedGroup,
//                    onGroupSelected = { group ->
//                        selectedGroup = group
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Информация о выбранной группе
//                selectedGroup?.let { group ->
//                    Card(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Text(
//                                text = "Выбрана группа:",
//                                style = MaterialTheme.typography.labelLarge,
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = group.groupName,
//                                style = MaterialTheme.typography.headlineMedium
//                            )
//                            Text(
//                                text = "${group.course} курс",
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                            Text(
//                                text = group.specialty,
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}