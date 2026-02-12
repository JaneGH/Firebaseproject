package com.example.firebaseproject.presentation.client

import com.example.firebaseproject.domain.model.Client

sealed class ClientUiState {

    object Idle : ClientUiState()

    object Loading : ClientUiState()

    data class Success(val client: Client) : ClientUiState()

    data class Error(val message: String?) : ClientUiState()
}