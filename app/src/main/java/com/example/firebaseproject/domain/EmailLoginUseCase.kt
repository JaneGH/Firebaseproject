package com.example.firebaseproject.domain

class EmailLoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.signInWithEmail(email, password)
    }
}