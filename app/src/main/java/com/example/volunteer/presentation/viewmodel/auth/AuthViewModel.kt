package com.example.volunteer.presentation.viewmodel.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.data.repository.AccountRepository
import com.example.volunteer.model.data.repository.AuthResult
import com.example.volunteer.model.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<AuthNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onToggleMode() {
        val current = _uiState.value
        val newMode = if (current.mode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
        _uiState.value = AuthUiState(
            mode = newMode,
            email = current.email,
            password = current.password
        )
    }

    fun onNameChange(value: String) {
        val error = if (value.isBlank()) "Name cannot be empty" else null
        _uiState.value = _uiState.value.copy(name = value, nameError = error)
    }

    fun onEmailChange(value: String) {
        val error = when {
            value.isBlank() -> "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(value.trim()).matches() -> "Enter a valid email"
            else -> null
        }
        _uiState.value = _uiState.value.copy(email = value, emailError = error)
    }

    fun onPasswordChange(value: String) {
        val error = if (value.length < 6) "Password must be at least 6 characters" else null
        val current = _uiState.value
        val confirmError = when {
            current.confirmPassword.isEmpty() -> null
            current.confirmPassword != value -> "Passwords do not match"
            else -> null
        }
        _uiState.value = current.copy(
            password = value,
            passwordError = error,
            confirmPasswordError = confirmError
        )
    }

    fun onConfirmPasswordChange(value: String) {
        val current = _uiState.value
        val error = if (value != current.password) "Passwords do not match" else null
        _uiState.value = current.copy(confirmPassword = value, confirmPasswordError = error)
    }

    fun onSubmit() {
        val state = _uiState.value
        if (!state.isFormValid || state.isSubmitting) return
        _uiState.value = state.copy(isSubmitting = true)
        viewModelScope.launch {
            val result = when (state.mode) {
                AuthMode.LOGIN ->
                    accountRepository.login(state.email, state.password)

                AuthMode.REGISTER ->
                    accountRepository.register(state.name, state.email, state.password)
            }
            when (result) {
                is AuthResult.Success -> {
                    sessionManager.setLoggedIn(result.accountId)
                    _navigationEvent.send(AuthNavigationEvent.NavigateToHome)
                }

                is AuthResult.Failure -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = false)
                    _navigationEvent.send(AuthNavigationEvent.ShowMessage(result.message))
                }
            }
        }
    }
}
