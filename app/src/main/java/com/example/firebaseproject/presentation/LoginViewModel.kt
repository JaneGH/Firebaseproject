package com.example.firebaseproject.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseproject.data.remote.GoogleAuthManager
import com.example.firebaseproject.domain.EmailLoginUseCase
import com.example.firebaseproject.domain.GoogleSignInUseCase
import com.example.firebaseproject.domain.SaveUserUseCase
import com.example.firebaseproject.domain.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val emailLoginUseCase: EmailLoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<String?>(null)
    val uiState: StateFlow<String?> = _uiState

    fun signInWithGoogle() {
        viewModelScope.launch {
            val result = googleSignInUseCase()
            result.fold(
                onSuccess = { user ->
                    saveUserUseCase(user)
                    _uiState.value = "Login Success"
                },
                onFailure = { error ->
                    _uiState.value = error.message
                }
            )
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            saveUserUseCase(user)
        }
    }

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            val result = emailLoginUseCase(email, password)
            _uiState.value = result.fold(
                onSuccess = { "Login Success" },
                onFailure = { it.message }
            )
        }
    }
}
