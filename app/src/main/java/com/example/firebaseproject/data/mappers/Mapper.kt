package com.example.firebaseproject.data.mappers

import com.example.firebaseproject.data.SyncStatus
import com.example.firebaseproject.data.local.entity.ClientEntity
import com.example.firebaseproject.data.local.entity.UserImageEntity
import com.example.firebaseproject.domain.model.Client
import com.example.firebaseproject.domain.model.UserImage
import kotlin.String

fun UserImageEntity.toDomain(): UserImage =
    UserImage(
        id = id,
        userId = userId,
        clientId = clientId,
        localPath = localPath,
        remoteUrl = remoteUrl,
        syncStatus = syncStatus,
        createdAt = createdAt
    )

fun ClientEntity.toDomain(): Client {
    return Client(
        id = id,
        fullName = fullName,
        age = age,
        address = address,
        avatarUri = avatarUri ?: "",
    )
}