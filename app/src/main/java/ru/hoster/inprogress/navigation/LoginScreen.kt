package ru.hoster.inprogress.navigation // Замените на ваш пакет, если отличается

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
// Импортируем константы Graph и Route из вашего файла NavigationConstants.kt
// Убедитесь, что импорты правильные для вашей структуры проекта
// import ru.hoster.inprogress.navigation.Graph
// import ru.hoster.inprogress.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LaunchedEffect(key1 = uiState.navigateToMainApp) {
        if (uiState.navigateToMainApp) {
            navController.navigate(Graph.MAIN_APPLICATION) {
                popUpTo(Graph.AUTHENTICATION) { inclusive = true }
            }
            viewModel.consumeNavigationTrigger() // Сбрасываем флаг
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Вход", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = uiState.errorMessage != null,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = uiState.errorMessage != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.loginUser() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Войти")
                }
            }
            TextButton(onClick = { navController.navigate(Route.SIGN_UP) }) {
                Text("Нет аккаунта? Зарегистрироваться")
            }
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}