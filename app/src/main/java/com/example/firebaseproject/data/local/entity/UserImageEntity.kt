package com.example.firebaseproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.firebaseproject.domain.SyncStatus

@Entity(tableName = "user_images")
data class UserImageEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userId: String,

    val clientId: String,

    val localPath: String,

    val remoteUrl: String?,

    val syncStatus: SyncStatus,

    val createdAt: Long
)