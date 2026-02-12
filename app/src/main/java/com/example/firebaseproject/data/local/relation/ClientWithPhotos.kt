package com.example.firebaseproject.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.firebaseproject.data.local.entity.ClientEntity
import com.example.firebaseproject.data.local.entity.UserImageEntity

data class ClientWithPhotos(

    @Embedded
    val client: ClientEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "clientId"
    )
    val photos: List<UserImageEntity>
)
