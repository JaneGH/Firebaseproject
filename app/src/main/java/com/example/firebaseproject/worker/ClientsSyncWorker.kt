package com.example.firebaseproject.worker

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.firebaseproject.data.local.dao.ClientDao
import com.example.firebaseproject.domain.SyncStatus
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import java.io.File

@HiltWorker
class ClientsSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val clientDao: ClientDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        Log.d("WORKER", "ClientsSyncWorker started")

        val pendingClients = clientDao.getBySyncStatus(SyncStatus.PENDING)

        if (pendingClients.isEmpty()) {
            Log.d("WORKER", "No pending clients")
            return Result.success()
        }

        val database = FirebaseDatabase.getInstance().reference
        val storage = FirebaseStorage.getInstance().reference

        return try {

            for (client in pendingClients) {

                Log.d("WORKER", "Syncing client ${client.id}")

                var remoteAvatarUrl: String? = null

                client.avatarUri?.let { localPath ->

                    val file = File(localPath)

                    if (file.exists()) {

                        val storageRef =
                            storage.child("clients/${client.id}/avatar.jpg")

                        storageRef.putFile(Uri.fromFile(file)).await()

                        remoteAvatarUrl =
                            storageRef.downloadUrl.await().toString()

                        file.delete()

                    } else {
                        Log.e("WORKER", "Local avatar not found: $localPath")
                    }
                }

                val clientData = mapOf(
                    "id" to client.id,
                    "fullName" to client.fullName,
                    "age" to client.age,
                    "address" to client.address,
                    "avatarUrl" to remoteAvatarUrl,
                    "createdAt" to client.createdAt
                )

                database.child("clients")
                    .child(client.id)
                    .setValue(clientData)
                    .await()

                 clientDao.update(
                    client.copy(
                        avatarUri = remoteAvatarUrl,
                        syncStatus = SyncStatus.SYNCED
                    )
                )

                Log.d("WORKER", "Client ${client.id} synced successfully")
            }

            Result.success()

        } catch (e: Exception) {

            if (e is StorageException) {
                Log.e("WORKER", "Storage error code: ${e.errorCode}")
                Log.e("WORKER", "HTTP result: ${e.httpResultCode}")
            }

            Log.e("WORKER", "Sync failed", e)

            Result.retry()
        }
    }
}
