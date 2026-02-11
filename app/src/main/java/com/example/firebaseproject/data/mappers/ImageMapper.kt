package com.example.firebaseproject.data.mappers

import com.example.firebaseproject.data.local.entity.UserImageEntity
import com.example.firebaseproject.domain.model.UserImage

fun UserImageEntity.toDomain(): UserImage =
    UserImage(
        id = id,
        userId = userId,
        localPath = localPath,
        remoteUrl = remoteUrl,
        syncStatus = syncStatus,
        createdAt = createdAt
    )