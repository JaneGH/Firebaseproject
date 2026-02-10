package com.example.firebaseproject.domain.usecase

import com.example.firebaseproject.domain.repository.AuthRepository
import com.example.firebaseproject.domain.User

class GoogleSignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return repository.signInWithGoogle()
    }
}