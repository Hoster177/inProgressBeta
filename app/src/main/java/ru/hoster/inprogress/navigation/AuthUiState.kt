package ru.hoster.inprogress.navigation

import com.google.firebase.auth.FirebaseUser

// Событие для навигации, которое должно обрабатываться один раз
sealed class AuthNavigationEvent {
    object NavigateToMain : AuthNavigationEvent()
    //object NavigateToSignUp : AuthNavigationEvent() // Если нужно из LoginScreen
    object NavigateToLogin : AuthNavigationEvent() // Если нужно из SignUpScreen
}
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // isAuthenticated и user могут дублировать друг друга,
    // но оставим для совместимости с предыдущей логикой.
    // user != null является более надежным индикатором аутентификации с Firebase.
    val isAuthenticated: Boolean = false,
    val user: FirebaseUser? = null,
    val navigateToMainApp: Boolean = false // Флаг для навигации
)