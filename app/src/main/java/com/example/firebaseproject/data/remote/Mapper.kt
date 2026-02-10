package com.example.firebaseproject.data.remote

import com.example.firebaseproject.domain.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): User {
    return User(
        uid = uid,
        name = displayName ?: "",
        email = email ?: ""
    )
}