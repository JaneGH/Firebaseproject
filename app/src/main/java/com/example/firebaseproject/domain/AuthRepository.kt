package com.example.firebaseproject.domain

interface AuthRepository {
    suspend fun signInWithGoogle(): Result<Unit>
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    fun isLoggedIn(): Boolean
}