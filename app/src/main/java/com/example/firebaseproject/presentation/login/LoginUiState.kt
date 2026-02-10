package com.example.firebaseproject.presentation.login

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String?) : LoginUiState()
}