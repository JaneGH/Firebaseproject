package com.example.firebaseproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebaseproject.data.local.entity.UserImageEntity
import com.example.firebaseproject.domain.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UserImagesDao {

    @Insert
    suspend fun insert(entity: UserImageEntity): Long

    @Query("SELECT * FROM user_images WHERE userId = :uid ORDER BY createdAt DESC")
    fun observe(uid: String): Flow<List<UserImageEntity>>

    @Query("""
        SELECT * FROM user_images
        WHERE userId = :uid AND syncStatus IN ('PENDING','FAILED')
        ORDER BY createdAt ASC
    """)
    suspend fun getPending(uid: String): List<UserImageEntity>

    @Query("UPDATE user_images SET syncStatus = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: SyncStatus)

    @Query("UPDATE user_images SET syncStatus='SYNCED', remoteUrl=:url WHERE id=:id")
    suspend fun markSynced(id: Long, url: String)

    @Query("UPDATE user_images SET syncStatus='FAILED' WHERE id=:id")
    suspend fun markFailed(id: Long)
}