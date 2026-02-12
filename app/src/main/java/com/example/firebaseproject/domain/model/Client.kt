package com.example.firebaseproject.domain.model

data class Client(
    val id:String,
    val fullName: String,
    val age: Int,
    val address: String = "",
    val avatarUri: String ="",
)