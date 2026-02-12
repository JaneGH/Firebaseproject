package com.example.firebaseproject.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.firebaseproject.data.SyncStatus

@Entity(tableName = "user_images",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("clientId")]
    )
data class UserImageEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userId: String,

    val clientId: String,

    val localPath: String,

    val remoteUrl: String = "",

    val syncStatus: SyncStatus,

    val createdAt: Long
)