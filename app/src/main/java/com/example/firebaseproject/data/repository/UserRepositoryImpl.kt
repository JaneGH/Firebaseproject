package com.example.firebaseproject.data.repository

import com.example.firebaseproject.domain.model.User
import com.example.firebaseproject.domain.repository.UserRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : UserRepository {

    override suspend fun saveUser(user: User) {
        database.reference
            .child("users")
            .child(user.uid)
            .setValue(user)
            .await()
    }
}