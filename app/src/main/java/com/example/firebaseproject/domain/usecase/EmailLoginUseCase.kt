package com.example.firebaseproject.domain.usecase

import com.example.firebaseproject.domain.repository.AuthRepository

class EmailLoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.signInWithEmail(email, password)
    }
}