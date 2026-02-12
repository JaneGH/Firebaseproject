package com.example.firebaseproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.firebaseproject.data.local.entity.UserImageEntity
import com.example.firebaseproject.data.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UserImagesDao {

    @Insert
    suspend fun insert(entity: UserImageEntity): Long

    @Insert
    suspend fun insertAll(photos: List<UserImageEntity>)

    @Update
    suspend fun update(photo: UserImageEntity)

    @Query("SELECT * FROM user_images WHERE userId = :uid ORDER BY createdAt DESC")
    fun observe(uid: String): Flow<List<UserImageEntity>>

    @Query("""
        SELECT * FROM user_images
        WHERE userId = :uid AND syncStatus IN ('PENDING','FAILED')
        ORDER BY createdAt ASC
    """)
    suspend fun getPending(uid: String): List<UserImageEntity>

    @Query("""
    SELECT * FROM user_images
    WHERE clientId = :clientId
    AND syncStatus IN ('PENDING','FAILED')
""")
    suspend fun getPendingByClient(clientId: String): List<UserImageEntity>

    @Query("UPDATE user_images SET syncStatus = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: SyncStatus)

    @Query("SELECT * FROM user_images WHERE clientId = :clientId")
    suspend fun getByClientId(clientId: String): List<UserImageEntity>

    @Query("UPDATE user_images SET syncStatus='SYNCED', remoteUrl=:url WHERE id=:id")
    suspend fun markSynced(id: Long, url: String)

    @Query("UPDATE user_images SET syncStatus='FAILED' WHERE id=:id")
    suspend fun markFailed(id: Long)
}