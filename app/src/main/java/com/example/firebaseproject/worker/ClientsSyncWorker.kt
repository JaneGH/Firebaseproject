package com.example.firebaseproject.worker

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.firebaseproject.data.SyncStatus
import com.example.firebaseproject.data.local.dao.ClientDao
import com.example.firebaseproject.data.local.dao.UserImagesDao
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
    private val clientDao: ClientDao,
    private val imagesDao: UserImagesDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        Log.d("WORKER", "ClientsSyncWorker started")

        val pendingClients =
            clientDao.getBySyncStatus(SyncStatus.PENDING)

        if (pendingClients.isEmpty()) {
            return Result.success()
        }

        val storage = FirebaseStorage.getInstance().reference
        val database = FirebaseDatabase.getInstance().reference

        return try {

            for (client in pendingClients) {

                Log.d("WORKER", "Syncing client ${client.id}")

                val remoteAvatarUrl =
                    uploadAvatar(client.id, client.avatarUri, storage)

                uploadGalleryImages(client.id, storage)

                uploadClientData(
                    client = client,
                    remoteAvatarUrl = remoteAvatarUrl,
                    database = database
                )

                markClientSynced(client, remoteAvatarUrl)

                Log.d("WORKER", "Client ${client.id} synced")
            }

            Result.success()

        } catch (e: Exception) {

            if (e is StorageException) {
                Log.e("WORKER", "Storage error: ${e.errorCode}")
            }

            Log.e("WORKER", "Sync failed", e)
            Result.retry()
        }
    }


    private suspend fun uploadAvatar(
        clientId: String,
        localPath: String?,
        storage: com.google.firebase.storage.StorageReference
    ): String? {

        if (localPath.isNullOrBlank()) return null

        val file = File(localPath)
        if (!file.exists()) return null

        val avatarRef =
            storage.child("clients/$clientId/avatar.jpg")

        avatarRef.putFile(Uri.fromFile(file)).await()

        val remoteUrl =
            avatarRef.downloadUrl.await().toString()

        file.delete()

        return remoteUrl
    }


    private suspend fun uploadGalleryImages(
        clientId: String,
        storage: com.google.firebase.storage.StorageReference
    ) {

        val pendingImages =
            imagesDao.getPendingByClient(clientId)

        for (image in pendingImages) {

            val file = File(image.localPath)
            if (!file.exists()) continue

            try {

                val imageRef =
                    storage.child("clients/$clientId/gallery/${file.name}")

                imageRef.putFile(Uri.fromFile(file)).await()

                val remoteUrl =
                    imageRef.downloadUrl.await().toString()

                imagesDao.markSynced(image.id, remoteUrl)

                file.delete()

            } catch (e: Exception) {
                imagesDao.markFailed(image.id)
            }
        }
    }

    // -------------------------
    // Upload Client Data
    // -------------------------

    private suspend fun uploadClientData(
        client: com.example.firebaseproject.data.local.entity.ClientEntity,
        remoteAvatarUrl: String?,
        database: com.google.firebase.database.DatabaseReference
    ) {

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
    }

    private suspend fun markClientSynced(
        client: com.example.firebaseproject.data.local.entity.ClientEntity,
        remoteAvatarUrl: String?
    ) {

        clientDao.update(
            client.copy(
                avatarUri = remoteAvatarUrl,
                syncStatus = SyncStatus.SYNCED
            )
        )
    }
}
