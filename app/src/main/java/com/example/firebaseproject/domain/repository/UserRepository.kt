package com.example.firebaseproject.domain.repository

import com.example.firebaseproject.domain.model.User

interface UserRepository {
    suspend fun saveUser(user: User)
}