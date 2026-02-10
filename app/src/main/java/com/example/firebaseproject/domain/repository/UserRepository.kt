package com.example.firebaseproject.domain.repository

import com.example.firebaseproject.domain.User

interface UserRepository {
    suspend fun saveUser(user: User)
}