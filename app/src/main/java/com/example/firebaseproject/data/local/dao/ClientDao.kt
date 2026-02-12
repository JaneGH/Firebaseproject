package com.example.firebaseproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.firebaseproject.data.local.entity.ClientEntity
import com.example.firebaseproject.data.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: ClientEntity)

    @Query("SELECT * FROM clients")
    fun observeAll(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE syncStatus = :status")
    suspend fun getBySyncStatus(status: SyncStatus): List<ClientEntity>

    @Update
    suspend fun update(client: ClientEntity)
}
