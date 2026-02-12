package com.example.firebaseproject.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.firebaseproject.domain.SyncStatus

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val age: Int,
    val address: String,
    val avatarUri: String?,
    val createdAt: Long,
    val syncStatus: SyncStatus
)