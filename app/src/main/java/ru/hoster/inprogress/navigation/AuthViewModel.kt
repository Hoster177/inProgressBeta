package ru.hoster.inprogress.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = mutableStateOf(AuthUiState())
    val uiState: State<AuthUiState> = _uiState

    init {
        // Проверка текущего пользователя при инициализации ViewModel.
        // Это поможет определить начальное состояние, если ViewModel создается
        // до того, как MainActivity проверит FirebaseAuth.getInstance().currentUser.
        auth.currentUser?.let {
            _uiState.value = AuthUiState(
                isAuthenticated = true,
                user = it,
                navigateToMainApp = false // false, т.к. начальная навигация управляется MainActivity
            )
        }
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun loginUser() {
        if (uiState.value.email.isBlank() || uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email и пароль не могут быть пустыми")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch { // Не обязательно, т.к. addOnCompleteListener асинхронен, но не повредит
            auth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            user = auth.currentUser,
                            navigateToMainApp = true // Устанавливаем флаг для навигации
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = task.exception?.message ?: "Ошибка входа"
                        )
                    }
                }
        }
    }

    fun signUpUser() {
        if (uiState.value.email.isBlank() || uiState.value.password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Email и пароль не могут быть пустыми")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch { // Аналогично loginUser
            auth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            user = auth.currentUser,
                            navigateToMainApp = true // Устанавливаем флаг для навигации
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = task.exception?.message ?: "Ошибка регистрации"
                        )
                    }
                }
        }
    }

    fun consumeNavigationTrigger() {
        _uiState.value = _uiState.value.copy(navigateToMainApp = false)
    }
}