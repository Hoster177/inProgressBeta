package ru.hoster.inprogress.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.hoster.inprogress.domain.model.AuthService
import javax.inject.Inject

sealed class AuthState {
    object Unknown : AuthState() // Начальное состояние, пока мы не знаем, вошел ли пользователь
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    authService: AuthService
) : ViewModel() {

    // Этот Flow будет эмитить true, если пользователь вошел, false если нет
    val authState: StateFlow<AuthState> = authService.isUserLoggedIn()
        .map { isLoggedIn ->
            if (isLoggedIn) AuthState.Authenticated else AuthState.Unauthenticated
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Начать эмитить, когда есть подписчики
            initialValue = AuthState.Unknown // Начальное значение, пока Firebase не ответил
        )
}
