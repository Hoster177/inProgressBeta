package ru.hoster.inprogress.navigation // Ваш пакет

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.* // Для Composable, LaunchedEffect, collectAsState, mutableStateOf, remember, State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("Русский") }

    // Строка 27, где была ошибка:
    val authUiState: AuthUiState by authViewModel.uiState.collectAsState()
    // Явно указал тип AuthUiState для authUiState, хотя collectAsState должен его выводить.
    // Это может помочь компилятору, если есть проблемы с выводом типов.

    LaunchedEffect(key1 = authUiState.navigationEvent) {
        // Проверяем, что AuthNavigationEvent импортирован правильно
        if (authUiState.navigationEvent is AuthNavigationEvent.NavigateToLogin) {
            // Логика навигации при выходе (основная навигация должна управляться сменой графа в FinalRootNavHost)
            // navController.navigate(Graph.AUTHENTICATION) { // Убедитесь, что Graph.AUTHENTICATION это правильный роут
            //    popUpTo(Graph.MAIN_APPLICATION) { inclusive = true } // Убедитесь, что Graph.MAIN_APPLICATION это правильный роут
            // }
            authViewModel.onNavigationEventConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Общие настройки приложения", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Text("Язык приложения", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selectedLanguage == "Русский", onClick = { selectedLanguage = "Русский" })
                Text("Русский", modifier = Modifier.padding(start = 8.dp))
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = selectedLanguage == "English", onClick = { selectedLanguage = "English" })
                Text("English", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Уведомления", style = MaterialTheme.typography.titleMedium)
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authUiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        authViewModel.signOut()
                        // Навигация должна произойти автоматически из-за изменения AuthState в MainViewModel
                        // и последующей рекомпозиции FinalRootNavHost, который сменит граф.
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Выйти из аккаунта")
                }
            }

            authUiState.errorMessage?.let { error ->
                Text(
                    text = "Ошибка выхода: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { /* TODO: Show User Agreement */ }) {
                Text("Пользовательское соглашение")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserSettingsScreenPreview() {
    // Для превью MaterialTheme должен оборачивать контент.
    MaterialTheme {
        // Для превью, если Hilt не активен, AuthViewModel не будет внедрен.
        // Можно создать фейковый ViewModel или передать фейковый NavController.
        // HiltViewModel() в превью может вызвать проблемы, если Hilt не настроен для превью.
        // Простейший вариант - закомментировать viewModel в превью или использовать фейк.
        UserSettingsScreen(
            navController = rememberNavController()
            // authViewModel = // Здесь нужен фейковый ViewModel для превью или используйте превью без ViewModel
        )
    }
}