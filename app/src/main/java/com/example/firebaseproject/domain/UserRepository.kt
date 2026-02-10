package com.example.firebaseproject.domain

interface UserRepository {
    suspend fun saveUser(user: User)
}