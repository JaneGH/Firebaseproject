package com.example.firebaseproject.data.storage

interface ImageStorage {
    suspend fun saveImage(uri: String): String
}