package com.example.firebaseproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.firebaseproject.data.local.converter.SyncStatusConverter
import com.example.firebaseproject.data.local.dao.ClientDao
import com.example.firebaseproject.data.local.dao.UserImagesDao
import com.example.firebaseproject.data.local.entity.ClientEntity
import com.example.firebaseproject.data.local.entity.UserImageEntity

@Database(
    entities = [UserImageEntity::class, ClientEntity::class ],
    version = 1
)
@TypeConverters(SyncStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userImagesDao(): UserImagesDao
    abstract fun clientDao(): ClientDao
}