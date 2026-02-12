package com.example.firebaseproject.data.local.converter

import androidx.room.TypeConverter
import com.example.firebaseproject.data.SyncStatus

class SyncStatusConverter {

    @TypeConverter
    fun fromStatus(status: SyncStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): SyncStatus =
        SyncStatus.valueOf(value)
}