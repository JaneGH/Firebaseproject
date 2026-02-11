package com.example.firebaseproject.domain.repository

import com.example.firebaseproject.domain.model.UserImage
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun observeImages(uid: String): Flow<List<UserImage>>
    suspend fun addImageFromLocalPath(uid: String, localPath: String)
    fun enqueueSync(uid: String)
}
