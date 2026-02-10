package com.example.firebaseproject.domain.repository

import com.example.firebaseproject.domain.User

interface AuthRepository {
    suspend fun signInWithGoogle(): Result<User>
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    fun isLoggedIn(): Boolean
}