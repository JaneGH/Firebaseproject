package com.example.firebaseproject.domain.model

import com.example.firebaseproject.domain.SyncStatus

data class UserImage(
    val id: Long,
    val userId: String,
    val localPath: String,
    val remoteUrl: String?,
    val syncStatus: SyncStatus,
    val createdAt: Long
)