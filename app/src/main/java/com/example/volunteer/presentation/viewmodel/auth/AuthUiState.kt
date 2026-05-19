package com.example.volunteer.presentation.viewmodel.auth

enum class AuthMode { LOGIN, REGISTER }

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isSubmitting: Boolean = false
) {
    val isFormValid: Boolean
        get() = when (mode) {
            AuthMode.LOGIN ->
                email.isNotBlank() && password.isNotBlank() &&
                    emailError == null && passwordError == null

            AuthMode.REGISTER ->
                name.isNotBlank() && email.isNotBlank() &&
                    password.isNotBlank() && confirmPassword.isNotBlank() &&
                    nameError == null && emailError == null &&
                    passwordError == null && confirmPasswordError == null
        }
}
