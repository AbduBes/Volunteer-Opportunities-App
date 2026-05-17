package com.example.volunteer.presentation.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.volunteer.presentation.viewmodel.auth.AuthMode
import com.example.volunteer.presentation.viewmodel.auth.AuthNavigationEvent
import com.example.volunteer.presentation.viewmodel.auth.AuthUiState
import com.example.volunteer.presentation.viewmodel.auth.AuthViewModel
import kotlinx.coroutines.launch

// 1. Stateful Component (Logic & Integration)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is AuthNavigationEvent.NavigateToHome -> onAuthSuccess()
                is AuthNavigationEvent.ShowMessage ->
                    launch { snackbarHostState.showSnackbar(event.message) }
            }
        }
    }

    AuthScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onToggleMode = viewModel::onToggleMode,
        onSubmit = viewModel::onSubmit,
        modifier = modifier
    )
}

// 2. Stateless Component (Pure UI)
@Composable
private fun AuthScreenContent(
    uiState: AuthUiState,
    snackbarHostState: SnackbarHostState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleMode: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRegister = uiState.mode == AuthMode.REGISTER

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (isRegister) "Create Account" else "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isRegister)
                    "Sign up to start your volunteering journey."
                else
                    "Log in to continue your quests.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isRegister) {
                AuthTextField(
                    value = uiState.name,
                    onValueChange = onNameChange,
                    label = "Name",
                    errorMessage = uiState.nameError
                )
            }

            AuthTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = "Email",
                errorMessage = uiState.emailError,
                keyboardType = KeyboardType.Email
            )

            AuthTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = "Password",
                errorMessage = uiState.passwordError,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            if (isRegister) {
                AuthTextField(
                    value = uiState.confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = "Confirm Password",
                    errorMessage = uiState.confirmPasswordError,
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSubmit,
                enabled = uiState.isFormValid && !uiState.isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (isRegister) "Register" else "Log In")
                }
            }

            TextButton(
                onClick = onToggleMode,
                enabled = !uiState.isSubmitting
            ) {
                Text(
                    if (isRegister)
                        "Already have an account? Log in"
                    else
                        "Don't have an account? Register"
                )
            }
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorMessage != null,
        supportingText = {
            if (errorMessage != null) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth()
    )
}
