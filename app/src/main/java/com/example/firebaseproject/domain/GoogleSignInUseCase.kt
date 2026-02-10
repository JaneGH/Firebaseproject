package com.example.firebaseproject.domain

class GoogleSignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return repository.signInWithGoogle()
    }
}